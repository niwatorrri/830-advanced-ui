import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ArrayList;

public class SimpleGroup implements Group {
    private int x, y, width, height;
    private Group group = null;
    private List<GraphicalObject> children
        = new ArrayList<GraphicalObject>();

    /**
     * Constructors
     */
    public SimpleGroup(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public SimpleGroup() {
        this(0, 0, 200, 200);
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

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        // Intersect the clip shape with the group bounding box
        Shape commonClipArea = getBoundingBox().intersection(clipShape.getBounds());

        // Translate the new clip shape to pass to children
        AffineTransform transform = new AffineTransform();
        transform.translate(-x, -y);
        Shape childClipShape = transform.createTransformedShape(commonClipArea);

        // Translate the origin to draw children
        graphics.translate(x, y);
        for (GraphicalObject child: children) {
            child.draw(graphics, childClipShape);
        }
        graphics.translate(-x, -y);
    }

    public BoundaryRectangle getBoundingBox() {
        return new BoundaryRectangle(x, y, width, height);
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        if (this.group != null && group != null) {
            throw new AlreadyHasGroupRunTimeException();
        }
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
            throw new AlreadyHasGroupRunTimeException();
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
        int childX = pt.x - x;
        int childY = pt.y - y;
        return new Point(childX, childY);
    }

    public Point childToParent(Point pt) {
        int parentX = pt.x + x;
        int parentY = pt.y + y;
        return new Point(parentX, parentY);
    }
}