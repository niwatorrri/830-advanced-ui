import java.awt.*;
import java.util.ArrayList;

public class SimpleGroup implements Group {
    private int x, y, width, height;
    private Group group = null;
    private ArrayList<GraphicalObject> children;

    /* Constructors */
    public SimpleGroup(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        children = new ArrayList<GraphicalObject>();
    }

    public SimpleGroup() {
        // TODO: change params
        this(0, 0, 10, 10);
    }

    /* Getters and setters */
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

    /* Methods defined in the GraphicalObject interface */
    public void draw(Graphics2D graphics, Shape clipShape) {
        for (GraphicalObject object: children) {
            object.draw(graphics, clipShape);
        }
    }

    public BoundaryRectangle getBoundingBox() {
        // TODO: compute accurately
        return new BoundaryRectangle(x, y, width, height);
    }

    public void moveTo(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean contains(int x, int y) {
        // TODO: need to check correctness
        return getBoundingBox().contains(x, y);
    }

    /* Methods defined in the Group interface */
    public void addChild(GraphicalObject child) {
        // TODO: group already has object?
        if (child.getGroup() == null) {
            children.add(child);
            child.setGroup(this);
        } else {
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
            throw new RuntimeException("Object is not in group");
        }
    }

    public void resizeToChildren() {

    }

    public ArrayList<GraphicalObject> getChildren() {
        return null;
    }

    public Point parentToChild(Point pt) {
        int childX = (int)pt.getX() - x;
        int childY = (int)pt.getY() - y;
        return new Point(childX, childY);
    }

    public Point childToParent(Point pt) {
        int parentX = (int)pt.getX() + x;
        int parentY = (int)pt.getY() + y;
        return new Point(parentX, parentY);
    }
}