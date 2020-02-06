import java.awt.*;
import java.util.ArrayList;

public class SimpleGroup implements Group {
    private int x, y, width, height;
    private Group group = null;
    private ArrayList<GraphicalObject> children;

    /**
     * Constructors
     */
    public SimpleGroup(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        children = new ArrayList<GraphicalObject>();
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
        graphics.translate(x, y); // TODO: ?
        for (GraphicalObject child: children) {
            child.draw(graphics, getBoundingBox());
        }
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
        Point pt = new Point(x, y);
        if (group != null) { // TODO: ?
            pt = group.parentToChild(pt);
        } else {
            pt = this.group.childToParent(pt);
        }
        this.x = pt.x;
        this.y = pt.y;
        this.group = group;
    }

    public boolean contains(int x, int y) {
        return getBoundingBox().contains(x, y);
    }

    /**
     * Methods defined in the Group interface
     */
    public void addChild(GraphicalObject child) {
        // TODO: group already has object?
        Group childGroup = child.getGroup();
        if (childGroup == null) {
            children.add(child);
            child.setGroup(this);
        } else if (childGroup != this) {
            throw new RuntimeException("Object is already in a group");
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
        // TODO: correctness?
        int newWidth = width, newHeight = height;
        for (GraphicalObject child: children) {
            Rectangle box = child.getBoundingBox();
            newWidth = Math.max(newWidth, (int)box.getMaxX());
            newHeight = Math.max(newHeight, (int)box.getMaxY());
        }
        this.width = newWidth;
        this.height = newHeight;
    }

    public ArrayList<GraphicalObject> getChildren() {
        ArrayList<GraphicalObject> childrenCopy
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