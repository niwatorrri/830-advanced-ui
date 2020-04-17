package ui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import ui.toolkit.constraint.Constraint;
import ui.toolkit.graphics.object.FilledRect;
import ui.toolkit.graphics.object.Text;
import ui.toolkit.graphics.object.selectable.SelectableOutlineRect;

public class Box extends SelectableOutlineRect {
    private FilledRect fill;
    private Text label;
    private int type;

    public static final int OUTLINE = 0x1;
    public static final int FILLED = 0x2;

    public Box(String name, int x, int y, int width, int height,
                Color color, int lineThickness, int type) {
        super(x, y, width, height, color, lineThickness);

        this.fill = new FilledRect(x, y, width, height, Color.WHITE);
        this.label = new Text(name);
        this.label.setColor(color);
        this.type = type;

        // constraints for fill position
        fill.setX(new Constraint<Integer>(this.useX()) {
            public Integer getValue() {
                return getX();
            }
        });

        fill.setY(new Constraint<Integer>(this.useY()) {
            public Integer getValue() {
                return getY();
            }
        });

        // constraints for label position
        label.setX(new Constraint<Integer>(this.useX(), this.useWidth()) {
            public Integer getValue() {
                return getX() + (getWidth() - label.getWidth()) / 2;
            }
        });

        label.setY(new Constraint<Integer>(this.useY()) {
            public Integer getValue() {
                return getY() + label.getHeight();
            }
        });
    }

    public FilledRect getFill() {
        return this.fill;
    }

    public Text getLabel() {
        return this.label;
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        if ((this.type & FILLED) == FILLED) {
            fill.draw(graphics, clipShape);
        }
        if ((this.type & OUTLINE) == OUTLINE) {
            super.draw(graphics, clipShape);
        }
        if (getWidth() > 0 && getHeight() > 0) {
            label.draw(graphics, clipShape);
        }
    }
}