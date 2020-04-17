package ui.toolkit.behavior;

import java.awt.Color;
import java.awt.Font;

import ui.toolkit.constraint.Constraint;
import ui.toolkit.constraint.NoConstraint;
import ui.toolkit.constraint.SetupConstraint;
import ui.toolkit.graphics.object.GraphicalObject;
import ui.toolkit.graphics.object.Text;
import ui.toolkit.graphics.object.selectable.SelectableText;

public class NewTextBehavior extends NewBehavior {
    /**
     * NewTextBehavior: create new texts in the group
     */
    private String text;
    private Font font;
    private Color color;

    private Constraint<String> textConstraint = new NoConstraint<>();
    private Constraint<Font> fontConstraint = new NoConstraint<>();
    private Constraint<Color> colorConstraint = new NoConstraint<>();

    public NewTextBehavior(String text, Font font, Color color, SetupConstraint constraint) {
        super(false, constraint);
        this.text = text;
        this.font = font;
        this.color = color;
    }

    public NewTextBehavior(String text, Font font, Color color) {
        this(text, font, color, null);
    }

    public NewTextBehavior(String text) {
        this(text, Text.DEFAULT_FONT, Color.BLUE);
    }

    public NewTextBehavior() {
        this("Text", Text.DEFAULT_FONT, Color.BLUE);
    }

    /**
     * Implement abstract methods in NewBehavior class
     */
    public GraphicalObject make(int x, int y, int uselessX, int uselessY, SetupConstraint constraint) {
        SelectableText t = new SelectableText(text, x, y, font, color);
        if (constraint != null) {
            constraint.setup(t);
        }
        return t;
    }

    public void resize(GraphicalObject object, int x, int y, int newX, int newY) {
        Text t = (Text) object;
        t.setX(newX);
        t.setY(newY);
    }

    public boolean isTrivial(GraphicalObject object) {
        return false;
    }

    /**
     * Getters and setters
     */
    public String getText() {
        if (textConstraint.isConstrained()) {
            this.text = textConstraint.evaluate();
        }
        return this.text;
    }

    public void setText(String text) {
        if (this.text != text) {
            if (!textConstraint.isConstrained()) {
                this.text = text;
                textConstraint.notifyValueChange(false);
            } else if (textConstraint.hasCycle()) {
                textConstraint.setValue(text);
                textConstraint.notifyValueChange(false);
            }
        }
    }

    public void setText(Constraint<String> constraint) {
        textConstraint.replaceWithConstraint(constraint);
        textConstraint = constraint;
        textConstraint.setValue(this.text);
        textConstraint.notifyValueChange(true);
    }

    public Constraint<String> useText() {
        return this.textConstraint;
    }

    public Font getFont() {
        if (fontConstraint.isConstrained()) {
            this.font = fontConstraint.evaluate();
        }
        return this.font;
    }

    public void setFont(Font font) {
        if (this.font != font) {
            if (!fontConstraint.isConstrained()) {
                this.font = font;
                fontConstraint.notifyValueChange(false);
            } else if (fontConstraint.hasCycle()) {
                fontConstraint.setValue(font);
                fontConstraint.notifyValueChange(false);
            }
        }
    }

    public void setFont(Constraint<Font> constraint) {
        fontConstraint.replaceWithConstraint(constraint);
        fontConstraint = constraint;
        fontConstraint.setValue(this.font);
        fontConstraint.notifyValueChange(true);
    }

    public Constraint<Font> useFont() {
        return this.fontConstraint;
    }

    public Color getColor() {
        if (colorConstraint.isConstrained()) {
            this.color = colorConstraint.evaluate();
        }
        return this.color;
    }

    public void setColor(Color color) {
        if (this.color != color) {
            if (!colorConstraint.isConstrained()) {
                this.color = color;
                colorConstraint.notifyValueChange(false);
            } else if (colorConstraint.hasCycle()) {
                colorConstraint.setValue(color);
                colorConstraint.notifyValueChange(false);
            }
        }
    }

    public void setColor(Constraint<Color> constraint) {
        colorConstraint.replaceWithConstraint(constraint);
        colorConstraint = constraint;
        colorConstraint.setValue(this.color);
        colorConstraint.notifyValueChange(true);
    }

    public Constraint<Color> useColor() {
        return this.colorConstraint;
    }
}