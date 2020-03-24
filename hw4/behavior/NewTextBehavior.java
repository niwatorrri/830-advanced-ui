package behavior;

import java.awt.Color;
import java.awt.Font;

import graphics.object.Text;
import graphics.object.GraphicalObject;
import graphics.object.selectable.SelectableText;
import constraint.SetupConstraint;

public class NewTextBehavior extends NewBehavior {
    /**
     * NewTextBehavior: create new texts in the group
     */
    private String text;
    private Font font;
    private Color color;

    public NewTextBehavior(String text, Font font, Color color, SetupConstraint<?> constraint) {
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
     * Getters and setters
     */
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Implement abstract methods in NewBehavior class
     */
    @SuppressWarnings("unchecked")
    public GraphicalObject make(int x, int y, int uselessX, int uselessY, SetupConstraint<?> constraint) {
        SelectableText t = new SelectableText(null, text, x, y, font, color);
        if (constraint != null) {
            ((SetupConstraint<SelectableText>) constraint).setup(t);
        }
        return t;
    }

    public void resize(GraphicalObject object, int x, int y, int newX, int newY) {
        Text text = (Text) object;
        text.setX(newX);
        text.setY(newY);
    }

    public boolean isTrivial(GraphicalObject object) {
        return false;
    }
}