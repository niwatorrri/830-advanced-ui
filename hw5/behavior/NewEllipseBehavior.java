package behavior;

import java.awt.Color;

import graphics.object.GraphicalObject;
import graphics.object.selectable.SelectableGraphicalObject;
import graphics.object.selectable.SelectableEllipse;
import graphics.object.selectable.SelectableFilledEllipse;
import graphics.object.Rect;
import constraint.SetupConstraint;

public class NewEllipseBehavior extends NewBehavior {
    /**
     * NewEllipseBehavior: create new ellipses in the group
     */
    private int type;
    private Color color;
    private int lineThickness;

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
     * Getters and setters
     */
    public int getType() {
        return this.type;
    }

    public NewEllipseBehavior setType(int type) {
        this.type = type;
        return this;
    }

    public Color getColor() {
        return this.color;
    }

    public NewEllipseBehavior setColor(Color color) {
        this.color = color;
        return this;
    }

    public int getLineThickness() {
        return this.lineThickness;
    }

    public NewEllipseBehavior setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
        return this;
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
}