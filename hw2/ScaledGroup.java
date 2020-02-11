import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ArrayList;

public class ScaledGroup implements Group {
    private int x, y, width, height;
    private double scaleX, scaleY;
    private Group group = null;
    private List<GraphicalObject> children;

    /**
     * Constructors
     */
    public ScaledGroup(int x, int y, int width, int height,
            double scaleX, double scaleY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        children = new ArrayList<GraphicalObject>();
    }

    public ScaledGroup() {
        this(0, 0, 200, 200, 1.0, 1.0);
    }

    /**
     * Getters and setters
     */
    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getScaleX() {
        return this.scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return this.scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        // Intersect the clip shape with the group bounding box
        Shape commonClipArea = getBoundingBox().intersection(clipShape.getBounds());

        // Transform the new clip shape to pass to children
        // Note: transforms are right-associative
        AffineTransform shapeTransform = new AffineTransform();
        shapeTransform.scale(1.0 / scaleX, 1.0 / scaleY);
        shapeTransform.translate(-x, -y);
        Shape childClipShape = shapeTransform.createTransformedShape(commonClipArea);

        // Transform the graphics to draw children
        AffineTransform oldTransform = graphics.getTransform();
        graphics.translate(x, y);           // 1. translate the origin
        graphics.scale(scaleX, scaleY);     // 2. scale the graphics
        for (GraphicalObject child: children) {
            child.draw(graphics, childClipShape);
        }
        graphics.setTransform(oldTransform);
    }

    public BoundaryRectangle getBoundingBox() {
        return new BoundaryRectangle(
            x, y,
            width * scaleX,
            height * scaleY
        );
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean contains(int x, int y) {
        return getBoundingBox().contains(x, y);
    }

    /**
     * Methods defined in the Group interface
     */
    public void addChild(GraphicalObject child) {
        Group childGroup = child.getGroup();
        if (childGroup != null) {
            throw new RuntimeException("Object is already in a group");
        } else {
            children.add(child);
            child.setGroup(this);
        }
    }

    public void removeChild(GraphicalObject child) {
        children.remove(child);
        child.setGroup(null);
    }

    public void bringChildToFront(GraphicalObject child) {
        if (children.remove(child)) {
            children.add(child);
        } else {
            throw new RuntimeException("Object is not in the group");
        }
    }

    public void resizeToChildren() {
        int newWidth = 0, newHeight = 0;
        for (GraphicalObject child: children) {
            Rectangle box = child.getBoundingBox();
            newWidth = Math.max(newWidth, (int)box.getMaxX());
            newHeight = Math.max(newHeight, (int)box.getMaxY());
        }
        this.width = newWidth;
        this.height = newHeight;
    }

    public List<GraphicalObject> getChildren() {
        List<GraphicalObject> childrenCopy
            = new ArrayList<GraphicalObject>(children);
        return childrenCopy;
    }

    public Point parentToChild(Point pt) {
        int childX = (int) ((pt.x - x) / scaleX);
        int childY = (int) ((pt.y - y) / scaleY);
        return new Point(childX, childY);
    }

    public Point childToParent(Point pt) {
        int parentX = (int) (pt.x * scaleX + x);
        int parentY = (int) (pt.y * scaleY + y);
        return new Point(parentX, parentY);
    }
}