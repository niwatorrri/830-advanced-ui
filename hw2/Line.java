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
        graphics.setStroke(new BasicStroke(
            (float)lineThickness,
            BasicStroke.CAP_BUTT,   // end cap style
            BasicStroke.JOIN_ROUND  // line join style
        ));
        graphics.drawLine(x1, y1, x2, y2);
        
        graphics.setClip(oldClip);
    }

    public BoundaryRectangle getBoundingBox() {
        // A relaxed bounding box
        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);
        double sinTheta = dy / Math.sqrt(dx * dx + dy * dy);
        double cosTheta = dx / Math.sqrt(dx * dx + dy * dy);
        return new BoundaryRectangle(
            Math.min(x1, x2) - lineThickness * sinTheta / 2,
            Math.min(y1, y2) - lineThickness * cosTheta / 2,
            dx + lineThickness * sinTheta,
            dy + lineThickness * cosTheta
        );
    }

    public void moveTo(int x, int y) {
        BoundaryRectangle boundingBox = getBoundingBox();
        int topLeftX = boundingBox.x;
        int topLeftY = boundingBox.y;
        this.x1 += x - topLeftX;
        this.x2 += x - topLeftX;
        this.y1 += y - topLeftY;
        this.y2 += y - topLeftY;
    }

    public Group getGroup() {
        return this.group;
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
}