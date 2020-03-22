package behavior;

import java.awt.Color;
import java.awt.Font;

import graphics.object.GraphicalObject;
import graphics.object.Text;

public class NewTextBehavior extends NewBehavior {
    private String text;
    private Font font;
    private Color color;

    public NewTextBehavior(String text, Font font, Color color) {
        super(true, false);
        this.text = text;
        this.font = font;
        this.color = color;
    }

    public NewTextBehavior() {
        this("Text", Text.DEFAULT_FONT, Color.BLACK);
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