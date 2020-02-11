import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ArrayList;

public class LayoutGroup implements Group {
    private int x, y, width, height;
    private int layout, offset;
    private Group group = null;
    private List<GraphicalObject> children;

    /**
     * Constructors
     */
    public LayoutGroup(int x, int y, int width, int height,
            int layout, int offset) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.layout = layout;
        this.offset = offset;
        children = new ArrayList<GraphicalObject>();
        checkIfSupportedLayout(layout);
    }

    public LayoutGroup() {
        this(0, 0, 200, 200, HORIZONTAL, 10);
    }

    private void checkIfSupportedLayout(int layout) {
        if ((layout != HORIZONTAL) && (layout != VERTICAL)) {
            throw new RuntimeException("Not supported layout type");
        }
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

    public int getLayout() {
        return this.layout;
    }

    public void setLayout(int layout) {
        checkIfSupportedLayout(layout);
        this.layout = layout;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
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
        int currentPosition = 0;
        for (GraphicalObject child: children) {
            BoundaryRectangle oldBoundingBox = child.getBoundingBox();
            if (layout == HORIZONTAL) {
                child.moveTo(currentPosition, 0);
                currentPosition += oldBoundingBox.width + offset;
            } else if (layout == VERTICAL) {
                child.moveTo(0, currentPosition);
                currentPosition += oldBoundingBox.height + offset;
            }
            child.draw(graphics, childClipShape);
            child.moveTo(oldBoundingBox.x, oldBoundingBox.y);
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
            Rectangle boundingBox = child.getBoundingBox();
            if (layout == HORIZONTAL) {
                newWidth += boundingBox.width + offset;
                newHeight = Math.max(newHeight, boundingBox.height);
            } else if (layout == VERTICAL) {
                newHeight += boundingBox.height + offset;
                newWidth = Math.max(newWidth, boundingBox.width);
            }
        }

        if (children.size() > 0) {
            if (layout == HORIZONTAL) {
                newWidth -= offset;
            } else if (layout == VERTICAL) {
                newHeight -= offset;
            }
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