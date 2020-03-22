package behavior;

import java.awt.Color;

import graphics.object.GraphicalObject;
import graphics.object.OutlineRect;
import graphics.object.FilledRect;
import graphics.object.Ellipse;
import graphics.object.Rect;

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

    public NewRectBehavior(int type, Color color, int lineThickness) {
        super(true);
        if (type == FILLED_RECT) {
            throw new RuntimeException("Incorrect constructor");
        }
        if (type != OUTLINE_RECT && type != ELLIPSE) {
            throw new RuntimeException("Unsupported rect type");
        }
        this.type = type;
        this.color = color;
        this.lineThickness = lineThickness;
    }

    public NewRectBehavior(int type, Color color) {
        super(true);
        if (type == OUTLINE_RECT || type == ELLIPSE) {
            throw new RuntimeException("Incorrect constructor");
        }
        if (type != FILLED_RECT) {
            throw new RuntimeException("Unsupported rect type");
        }
        this.type = type;
        this.color = color;
    }

    public NewRectBehavior() {
        this(OUTLINE_RECT, Color.BLACK, 1);
    }

    /**
     * Getters and setters
     */
    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
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
     * Implement abstract methods in NewBehavior class
     */
    public GraphicalObject make(int x, int y, int width, int height) {
        if (type == OUTLINE_RECT) {
            return new OutlineRect(x, y, width, height, color, lineThickness);
        } else if (type == FILLED_RECT) {
            return new FilledRect(x, y, width, height, color);
        } else{ // ellipse
            return new Ellipse(x, y, width, height, color, lineThickness);
        }
    }

    public void resize(GraphicalObject object, int x1, int y1, int x2, int y2) {
        Rect rect = (Rect) object;
        rect.setX(Math.min(x1, x2));
        rect.setY(Math.min(y1, y2));
        rect.setWidth(Math.abs(x1 - x2) + 1);
        rect.setHeight(Math.abs(y1 - y2) + 1);
    }

    public boolean isTrivial(GraphicalObject object) {
        Rect rect = (Rect) object;
        return rect.getWidth() <= 1 || rect.getHeight() <= 1;
    }
}