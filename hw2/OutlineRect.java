import java.awt.*;

public class OutlineRect implements GraphicalObject {
    private int x, y, width, height;
    private Color color;
    private int lineThickness;
    private Group group = null;

    /* Constructors */
    public OutlineRect(int x, int y, int width, int height,
            Color color, int lineThickness) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.lineThickness = lineThickness;
    }

    public OutlineRect() {
        // TODO: modify default params
        this(0, 0, 10, 10, Color.WHITE, 2);
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

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getLineThickness() {
        return this.lineThickness;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }

    /* Methods defined in the GraphicalObject interface */
    public void draw(Graphics2D graphics, Shape clipShape) {
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(lineThickness));
        graphics.drawRect(x, y, width, height);
        // TODO: clip to shape
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
        return this.group;
    }

	public void setGroup(Group group) {
        this.group = group;
    }

	public boolean contains(int x, int y) {
        // TODO: need to check correctness
        return getBoundingBox().contains(x, y);
    }
}