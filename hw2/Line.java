import java.awt.*;

public class Line implements GraphicalObject {
    private int x1, y1, x2, y2;
    private Color color;
    private int lineThickness;
    private Group group = null;

    /**
     * Constructors
     */
    public Line(int x1, int y1, int x2, int y2,
            Color color, int lineThickness) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.lineThickness = lineThickness;
    }

    public Line() {
        this(0, 0, 10, 10, Color.BLACK, 1);
    }

    /**
     * Getters and setters
     */
    public int getX1() {
        return this.x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return this.y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return this.x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return this.y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
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

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        Shape oldClip = graphics.getClip();
        graphics.setClip(clipShape);

        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(lineThickness));
        graphics.drawLine(x1, y1, x2, y2);
        
        graphics.setClip(oldClip);
    }

    public BoundaryRectangle getBoundingBox() {
        // TODO: compute bounds
        // int boxX = x - lineThickness / 2;
        // int boxY = y - lineThickness / 2;
        // int boxWidth = width + lineThickness - 1;
        // int boxHeight = height + lineThickness - 1;
        // return new BoundaryRectangle(boxX, boxY, boxWidth, boxHeight);
        return null;
    }

    public void moveTo(int x, int y) {
        this.x2 = x2 + x - x1;
        this.y2 = y2 + y - y1;
        this.x1 = x;
        this.y1 = y;
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean contains(int x, int y) {
        return getBoundingBox().contains(x, y);
    }
}