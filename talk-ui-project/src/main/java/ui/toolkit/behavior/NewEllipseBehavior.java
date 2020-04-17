package ui.toolkit.behavior;

import java.awt.Color;

import ui.toolkit.constraint.Constraint;
import ui.toolkit.constraint.NoConstraint;
import ui.toolkit.constraint.SetupConstraint;
import ui.toolkit.graphics.object.GraphicalObject;
import ui.toolkit.graphics.object.Rect;
import ui.toolkit.graphics.object.selectable.SelectableEllipse;
import ui.toolkit.graphics.object.selectable.SelectableFilledEllipse;
import ui.toolkit.graphics.object.selectable.SelectableGraphicalObject;

public class NewEllipseBehavior extends NewBehavior {
    /**
     * NewEllipseBehavior: create new ellipses in the group
     */
    private int type;
    private Color color;
    private int lineThickness;

    private Constraint<Integer> typeConstraint = new NoConstraint<>();
    private Constraint<Color> colorConstraint = new NoConstraint<>();
    private Constraint<Integer> lineThicknessConstraint = new NoConstraint<>();

    public static final int ELLIPSE = 0;
    public static final int FILLED_ELLIPSE = 1;

    /**
     * NewEllipseBehavior constructor
     * 
     * @param type          type of the new ellipse, either ELLIPSE or FILLED_ELLIPSE
     * @param color         color of the new ellipse
     * @param lineThickness line thickness of the new ellipse (only for ELLIPSE)
     * @param constraint    optional, constraint for the new ellipse
     */
    public NewEllipseBehavior(int type, Color color, int lineThickness, SetupConstraint constraint) {
        super(true, constraint);
        if (type != ELLIPSE && type != FILLED_ELLIPSE) {
            throw new RuntimeException("Unsupported ellipse type");
        }
        this.type = type;
        this.color = color;
        this.lineThickness = lineThickness;
    }

    public NewEllipseBehavior(int type, Color color, int lineThickness) {
        this(type, color, lineThickness, null);
    }

    public NewEllipseBehavior() {
        this(ELLIPSE, Color.BLACK, 1, null);
    }

    /**
     * Implement abstract methods in NewBehavior class
     */
    public GraphicalObject make(int x, int y, int width, int height, SetupConstraint constraint) {
        // create a new ellipse
        SelectableGraphicalObject o = null;
        if (type == ELLIPSE) {
            o = new SelectableEllipse(x, y, width, height, color, lineThickness);
        } else if (type == FILLED_ELLIPSE) {
            o = new SelectableFilledEllipse(x, y, width, height, color);
        } else {
            throw new RuntimeException("Unsupported ellipse type");
        }
        if (constraint != null) {
            constraint.setup(o);
        }
        return o;
    }

    public void resize(GraphicalObject object, int x1, int y1, int x2, int y2) {
        Rect r = (Rect) object;
        r.setX(Math.min(x1, x2));
        r.setY(Math.min(y1, y2));
        r.setWidth(Math.abs(x1 - x2) + 1);
        r.setHeight(Math.abs(y1 - y2) + 1);
    }

    public boolean isTrivial(GraphicalObject object) {
        Rect r = (Rect) object;
        return r.getWidth() <= 1 || r.getHeight() <= 1;
    }

    /**
     * Getters and setters
     */
    public int getType() {
        if (typeConstraint.isConstrained()) {
            this.type = typeConstraint.evaluate();
        }
        return this.type;
    }

    public NewEllipseBehavior setType(int type) {
        if (this.type != type) {
            if (!typeConstraint.isConstrained()) {
                this.type = type;
                typeConstraint.notifyValueChange(false);
            } else if (typeConstraint.hasCycle()) {
                typeConstraint.setValue(type);
                typeConstraint.notifyValueChange(false);
            }
        }
        return this;
    }

    public NewEllipseBehavior setType(Constraint<Integer> constraint) {
        typeConstraint.replaceWithConstraint(constraint);
        typeConstraint = constraint;
        typeConstraint.setValue(this.type);
        typeConstraint.notifyValueChange(true);
        return this;
    }

    public Constraint<Integer> useType() {
        return this.typeConstraint;
    }

    public Color getColor() {
        if (colorConstraint.isConstrained()) {
            this.color = colorConstraint.evaluate();
        }
        return this.color;
    }

    public NewEllipseBehavior setColor(Color color) {
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

    public NewEllipseBehavior setColor(Constraint<Color> constraint) {
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

    public NewEllipseBehavior setLineThickness(int lineThickness) {
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

    public NewEllipseBehavior setLineThickness(Constraint<Integer> constraint) {
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