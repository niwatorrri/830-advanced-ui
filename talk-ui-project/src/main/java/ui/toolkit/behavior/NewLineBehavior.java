package ui.toolkit.behavior;

import java.awt.Color;

import ui.toolkit.constraint.Constraint;
import ui.toolkit.constraint.NoConstraint;
import ui.toolkit.constraint.SetupConstraint;
import ui.toolkit.graphics.object.GraphicalObject;
import ui.toolkit.graphics.object.Line;
import ui.toolkit.graphics.object.selectable.SelectableLine;

public class NewLineBehavior extends NewBehavior {
    /**
     * NewLineBehavior: create new lines in the group
     */
    private Color color;
    private int lineThickness;

    private Constraint<Color> colorConstraint = new NoConstraint<>();
    private Constraint<Integer> lineThicknessConstraint = new NoConstraint<>();

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

    /**
     * Getters and setters
     */
    public Color getColor() {
        if (colorConstraint.isConstrained()) {
            this.color = colorConstraint.evaluate();
        }
        return this.color;
    }

    public NewLineBehavior setColor(Color color) {
        if (this.color != color) {
            if (!colorConstraint.isConstrained()) {
                this.color = color;
                colorConstraint.notifyValueChange(false);
            } else if (colorConstraint.hasCycle()) {
                colorConstraint.setValue(color);
                colorConstraint.notifyValueChange(false);
            }
        }
        return this;
    }

    public NewLineBehavior setColor(Constraint<Color> constraint) {
        colorConstraint.replaceWithConstraint(constraint);
        colorConstraint = constraint;
        colorConstraint.setValue(this.color);
        colorConstraint.notifyValueChange(true);
        return this;
    }

    public Constraint<Color> useColor() {
        return this.colorConstraint;
    }

    public int getLineThickness() {
        if (lineThicknessConstraint.isConstrained()) {
            this.lineThickness = lineThicknessConstraint.evaluate();
        }
        return this.lineThickness;
    }

    public NewLineBehavior setLineThickness(int lineThickness) {
        if (this.lineThickness != lineThickness) {
            if (!lineThicknessConstraint.isConstrained()) {
                this.lineThickness = lineThickness;
                lineThicknessConstraint.notifyValueChange(false);
            } else if (lineThicknessConstraint.hasCycle()) {
                lineThicknessConstraint.setValue(lineThickness);
                lineThicknessConstraint.notifyValueChange(false);
            }
        }
        return this;
    }

    public NewLineBehavior setLineThickness(Constraint<Integer> constraint) {
        lineThicknessConstraint.replaceWithConstraint(constraint);
        lineThicknessConstraint = constraint;
        lineThicknessConstraint.setValue(this.lineThickness);
        lineThicknessConstraint.notifyValueChange(true);
        return this;
    }

    public Constraint<Integer> useLineThickness() {
        return this.lineThicknessConstraint;
    }
}