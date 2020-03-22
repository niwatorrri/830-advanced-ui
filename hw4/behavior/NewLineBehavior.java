package behavior;

import java.awt.Color;

import graphics.object.GraphicalObject;
import graphics.object.Line;

public class NewLineBehavior extends NewBehavior {
    /**
     * NewLineBehavior: create new lines in the group
     */
    private Color color;
    private int lineThickness;

    public NewLineBehavior(Color color, int lineThickness) {
        super(false);
        this.color = color;
        this.lineThickness = lineThickness;
    }

    public NewLineBehavior() {
        this(Color.BLACK, 1);
    }

    /**
     * Getters and setters
     */
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
     * Implement abstract methods in NewBehavior class
     */
    public GraphicalObject make(int x1, int y1, int x2, int y2) {
        return new Line(x1, y1, x2, y2, color, lineThickness);
    }

    public void resize(GraphicalObject object, int x1, int y1, int x2, int y2) {
        Line line = (Line) object;
        line.setX2(x2);
        line.setY2(y2);
    }

    public boolean isTrivial(GraphicalObject object) {
        Line line = (Line) object;
        return (line.getX1() == line.getX2() && line.getY1() == line.getY2());
    }
}