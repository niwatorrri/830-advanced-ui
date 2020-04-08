package graphics.object;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import constraint.Constraint;
import constraint.NoConstraint;
import graphics.group.Group;

public class Text implements GraphicalObject {
    /**
     * Text class: texts
     */
    private String text;
    private int x, y;
    private Font font;
    private Color color;
    private Group group = null;

    private FontRenderContext context = new FontRenderContext(null, true, false);

    public static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    private Constraint<String> textConstraint = new NoConstraint<>();
    private Constraint<Integer> xConstraint = new NoConstraint<>();
    private Constraint<Integer> yConstraint = new NoConstraint<>();
    private Constraint<Font> fontConstraint = new NoConstraint<>();
    private Constraint<Color> colorConstraint = new NoConstraint<>();

    /**
     * Constructors
     */
    public Text(String text, int x, int y, Font font, Color color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.font = font;
        this.color = color;
    }

    public Text(String text, int x, int y) {
        this(text, x, y, DEFAULT_FONT, Color.BLACK);
    }

    public Text(String text) {
        this(text, 0, 0);
    }

    public Text() {
        this("Text");
    }

    /**
     * Getters, setters and "users"
     * 
     * Note: user (e.g. useX) returns the constraint on the variable (X)
     */    
    public int getX() {
        if (xConstraint.isConstrained()) {
            this.x = xConstraint.evaluate();
        }
        return this.x;
    }

    public void setX(int x) {
        if (this.x != x) {
            if (!xConstraint.isConstrained()) {
                this.x = x;
                xConstraint.notifyValueChange(false);
            } else if (xConstraint.hasCycle()) {
                // if no cycle, set a constrained x is no-op
                // if cycle, set local value and do multi-way constraint
                xConstraint.setValue(x);
                xConstraint.notifyValueChange(false);
            }
        }
    }

    public void setX(Constraint<Integer> constraint) {
        // update dependency graph for the new constraint
        xConstraint.replaceWithConstraint(constraint);
        xConstraint = constraint;
        xConstraint.setValue(this.x);
        xConstraint.notifyValueChange(true);
    }

    public Constraint<Integer> useX() {
        return this.xConstraint;
    }

    public int getY() {
        if (yConstraint.isConstrained()) {
            this.y = yConstraint.evaluate();
        }
        return this.y;
    }

    public void setY(int y) {
        if (this.y != y) {
            if (!yConstraint.isConstrained()) {
                this.y = y;
                yConstraint.notifyValueChange(false);
            } else if (yConstraint.hasCycle()) {
                yConstraint.setValue(y);
                yConstraint.notifyValueChange(false);
            }
        }
    }

    public void setY(Constraint<Integer> constraint) {
        yConstraint.replaceWithConstraint(constraint);
        yConstraint = constraint;
        yConstraint.setValue(this.y);
        yConstraint.notifyValueChange(true);
    }

    public Constraint<Integer> useY() {
        return this.yConstraint;
    }

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

    /**
     * Text-specific attributes
     */
    public int getAscent() {
        return (int) getFont().getLineMetrics("", context).getAscent();
    }

    public int getDescent() {
        return (int) getFont().getLineMetrics("", context).getDescent();
    }

    public int getLeading() {
        return (int) getFont().getLineMetrics("", context).getLeading();
    }

    public int getWidth() {
        return (int) getFont().getStringBounds(getText(), context).getWidth();
    }

    public int getHeight() {
        return (int) getFont().getLineMetrics("", context).getHeight();
    }

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        Shape oldClip = graphics.getClip();
        graphics.setClip(clipShape);

        RenderingHints oldRenderingHints = graphics.getRenderingHints();
        graphics.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        int x = getX(), y = getY();
        Font font = getFont();
        Color color = getColor();
        String text = getText();

        graphics.setFont(font);
        graphics.setColor(color);

        context = graphics.getFontRenderContext();
        int textHeight = (int) font.getLineMetrics("", context).getHeight();
        for (String line : text.split("\n")) {  // deal with newlines
            graphics.drawString(line, x, y);
            y += textHeight;
        }
        graphics.setClip(oldClip);
        graphics.setRenderingHints(oldRenderingHints);
    }

    public BoundaryRectangle getBoundingBox() {
        // The bounding box includes leading
        String text = getText();
        Font font = getFont();

        double totalWidth = 0, totalHeight = 0;
        Rectangle2D box = null;
        for (String line : text.split("\n")) { // deal with newlines
            box = font.getStringBounds(line, context);
            totalWidth = Math.max(totalWidth, box.getWidth());
            totalHeight += box.getHeight();
        }

        // Coordinates were relative to the reference point
        int x = getX(), y = getY();
        return new BoundaryRectangle(
            x + box.getX(), y + box.getY(),
            totalWidth, totalHeight
        );
    }

    public void moveTo(int x, int y) {
        BoundaryRectangle boundingBox = getBoundingBox();
        int topLeftX = boundingBox.x;
        int topLeftY = boundingBox.y;

        int prevX = getX(), prevY = getY();
        this.setX(prevX + x - topLeftX);
        this.setY(prevY + y - topLeftY);
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        if (this.group != null && group != null) {
            throw new AlreadyHasGroupRunTimeException();
        }
        this.group = group;
    }

    public boolean contains(int x, int y) {
        return getBoundingBox().contains(x, y);
    }

    public boolean contains(Point pt) {
        return contains(pt.x, pt.y);
    }
}