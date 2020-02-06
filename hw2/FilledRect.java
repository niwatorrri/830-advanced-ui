import java.awt.*;

public class FilledRect implements GraphicalObject {
    private int x, y, width, height;
    private Color color;
    private Group group = null;

    /**
     * Constructors
     */
    public FilledRect(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public FilledRect() {
        this(5, 5, 10, 10, Color.RED);
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

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        graphics.setColor(color);
        graphics.setClip(clipShape);
        graphics.fillRect(x, y, width, height);
    }

    public BoundaryRectangle getBoundingBox() {
        return new BoundaryRectangle(x, y, width, height);
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        Point pt = new Point(x, y);
        if (group != null) {
            pt = group.parentToChild(pt);
        } else { // TODO: ?
            pt = this.group.childToParent(pt);
        }
        this.x = pt.x;
        this.y = pt.y;
        this.group = group;
    }

    public boolean contains(int x, int y) {
        return getBoundingBox().contains(x, y);
    }
}