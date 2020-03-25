package behavior;

import java.awt.Color;

import graphics.object.GraphicalObject;
import graphics.object.selectable.SelectableOutlineRect;
import graphics.object.selectable.SelectableFilledRect;
import graphics.object.selectable.SelectableGraphicalObject;
import graphics.object.selectable.SelectableEllipse;
import graphics.object.Rect;
import constraint.SetupConstraint;

public class NewRectBehavior extends NewBehavior {
    /**
     * NewRectBehavior: create new rect-like objects in the group
     */
    private int type;
    private Color color;
    private int lineThickness;

    public static final int OUTLINE_RECT = 0;
    public static final int FILLED_RECT = 1;
    public static final int ELLIPSE = 2;

    public NewRectBehavior(int type, Color color, int lineThickness, SetupConstraint constraint) {
        super(true, constraint);
        if (type != OUTLINE_RECT && type != FILLED_RECT && type != ELLIPSE) {
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
        SelectableGraphicalObject o = null;
        switch (type) {
            case OUTLINE_RECT: {
                o = new SelectableOutlineRect(x, y, width, height, color, lineThickness);
                break;
            }
            case FILLED_RECT: {
                o = new SelectableFilledRect(x, y, width, height, color);
                break;
            }
            case ELLIPSE: {
                o = new SelectableEllipse(x, y, width, height, color, lineThickness);
                break;
            }
            default: {
                throw new RuntimeException("Unsupported rect type");
            }
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