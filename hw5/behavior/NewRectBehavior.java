package behavior;

import java.awt.Color;

import constraint.SetupConstraint;
import graphics.object.GraphicalObject;
import graphics.object.Rect;
import graphics.object.selectable.SelectableFilledRect;
import graphics.object.selectable.SelectableGraphicalObject;
import graphics.object.selectable.SelectableOutlineRect;

public class NewRectBehavior extends NewBehavior {
    /**
     * NewRectBehavior: create new rectangles in the group
     */
    private int type;
    private Color color;
    private int lineThickness;

    public static final int OUTLINE_RECT = 0;
    public static final int FILLED_RECT = 1;

    /**
     * NewRectBehavior constructor
     * 
     * @param type          type of the new rect, either OUTLINE_RECT or FILLED_RECT
     * @param color         color of the new rect
     * @param lineThickness line thickness of the new rect (only for OUTLINE_RECT)
     * @param constraint    optional, constraint for the new rect
     */
    public NewRectBehavior(int type, Color color, int lineThickness, SetupConstraint constraint) {
        super(true, constraint);
        if (type != OUTLINE_RECT && type != FILLED_RECT) {
            throw new RuntimeException("Unsupported rect type");
        }
        this.type = type;
        this.color = color;
        this.lineThickness = lineThickness;
    }

    public NewRectBehavior(int type, Color color, int lineThickness) {
        this(type, color, lineThickness, null);
    }

    public NewRectBehavior() {
        this(OUTLINE_RECT, Color.BLACK, 1, null);
    }

    /**
     * Getters and setters
     */
    public int getType() {
        return this.type;
    }

    public NewRectBehavior setType(int type) {
        this.type = type;
        return this;
    }

    public Color getColor() {
        return this.color;
    }

    public NewRectBehavior setColor(Color color) {
        this.color = color;
        return this;
    }

    public int getLineThickness() {
        return this.lineThickness;
    }

    public NewRectBehavior setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
        return this;
    }

    /**
     * Implement abstract methods in NewBehavior class
     */
    public GraphicalObject make(int x, int y, int width, int height, SetupConstraint constraint) {
        // create a new rectangle
        SelectableGraphicalObject o = null;
        if (type == OUTLINE_RECT) {
            o = new SelectableOutlineRect(x, y, width, height, color, lineThickness);
        } else if (type == FILLED_RECT) {
            o = new SelectableFilledRect(x, y, width, height, color);
        } else {
            throw new RuntimeException("Unsupported rect type");
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