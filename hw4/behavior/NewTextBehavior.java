package behavior;

import java.awt.Color;
import java.awt.Font;

import graphics.object.GraphicalObject;
import graphics.object.Text;

public class NewTextBehavior extends NewBehavior {
    /**
     * NewTextBehavior: create new texts in the group
     */
    private String text;
    private Font font;
    private Color color;

    public NewTextBehavior(String text, Font font, Color color) {
        super(false);
        this.text = text;
        this.font = font;
        this.color = color;
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
    public GraphicalObject make(int x, int y, int uselessX, int uselessY) {
        return new Text(null, text, x, y, font, color);
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