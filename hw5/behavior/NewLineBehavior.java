package behavior;

import java.awt.Color;

import constraint.SetupConstraint;
import graphics.object.GraphicalObject;
import graphics.object.Line;
import graphics.object.selectable.SelectableLine;

public class NewLineBehavior extends NewBehavior {
    /**
     * NewLineBehavior: create new lines in the group
     */
    private Color color;
    private int lineThickness;

    /**
     * NewLineBehavior constructor
     * 
     * @param color         color of the new lines
     * @param lineThickness line thickness of the new lines
     * @param constraint    optional, constraint for the new lines
     */
    public NewLineBehavior(Color color, int lineThickness, SetupConstraint constraint) {
        super(false, constraint);
        this.color = color;
        this.lineThickness = lineThickness;
    }

    public NewLineBehavior(Color color, int lineThickness) {
        this(color, lineThickness, null);
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

    public NewLineBehavior setColor(Color color) {
        this.color = color;
        return this;
    }

    public int getLineThickness() {
        return this.lineThickness;
    }

    public NewLineBehavior setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
        return this;
    }

    /**
     * Implement abstract methods in NewBehavior class
     */
    public GraphicalObject make(int x1, int y1, int x2, int y2, SetupConstraint constraint) {
        SelectableLine l = new SelectableLine(x1, y1, x2, y2, color, lineThickness);
        if (constraint != null) {
            constraint.setup(l);
        }
        return l;
    }

    public void resize(GraphicalObject object, int x1, int y1, int x2, int y2) {
        Line l = (Line) object;
        l.setX2(x2);
        l.setY2(y2);
    }

    public boolean isTrivial(GraphicalObject object) {
        Line l = (Line) object;
        return (l.getX1() == l.getX2() && l.getY1() == l.getY2());
    }
}