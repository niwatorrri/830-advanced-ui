import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Text implements GraphicalObject {
    /**
     * Text class: texts
     */
    private Graphics internalGraphics; // for calculation only
    private String text;
    private int x, y;
    private Font font;
    private Color color;
    private Group group = null;

    private static final Font DEFAULT_FONT
        = new Font("Monospaced", Font.PLAIN, 10);

    private Constraint<String> textConstraint = new Constraint<>();
    private Constraint<Integer> xConstraint = new Constraint<>();
    private Constraint<Integer> yConstraint = new Constraint<>();
    private Constraint<Font> fontConstraint = new Constraint<>();
    private Constraint<Color> colorConstraint = new Constraint<>();

    /**
     * Constructors
     */
    public Text(Graphics graphics, String text, int x, int y,
            Font font, Color color) {
        this.internalGraphics = graphics.create();
        this.text = text;
        this.x = x;
        this.y = y;
        this.font = font;
        this.color = color;

        internalGraphics.setFont(font);
        internalGraphics.setColor(color);
    }

    public Text() {
        this(null, "Text", 10, 10, DEFAULT_FONT, Color.BLACK);
    }

    /**
     * Getters, setters and "users"
     * 
     * Note: user (e.g. useX) returns the constraint on the variable (X)
     */
    public Graphics getGraphics() {
        return this.internalGraphics;
    }

    public void setGraphics(Graphics graphics) {
        this.internalGraphics = graphics;
    }
    
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
        xConstraint.updateConstraint(constraint);
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
        yConstraint.updateConstraint(constraint);
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
        textConstraint.updateConstraint(constraint);
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
            internalGraphics.setFont(this.font);
        }
        return this.font;
    }

    public void setFont(Font font) {
        if (this.font != font) {
            if (!fontConstraint.isConstrained()) {
                this.font = font;
                internalGraphics.setFont(this.font);
                fontConstraint.notifyValueChange(false);
            } else if (fontConstraint.hasCycle()) {
                fontConstraint.setValue(font);
                fontConstraint.notifyValueChange(false);
            }
        }
    }

    public void setFont(Constraint<Font> constraint) {
        fontConstraint.updateConstraint(constraint);
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
            internalGraphics.setColor(this.color);
        }
        return this.color;
    }

    public void setColor(Color color) {
        if (this.color != color) {
            if (!colorConstraint.isConstrained()) {
                this.color = color;
                internalGraphics.setColor(this.color);
                colorConstraint.notifyValueChange(false);
            } else if (colorConstraint.hasCycle()) {
                colorConstraint.setValue(color);
                colorConstraint.notifyValueChange(false);
            }
        }
    }

    public void setColor(Constraint<Color> constraint) {
        colorConstraint.updateConstraint(constraint);
        colorConstraint = constraint;
        colorConstraint.setValue(this.color);
        colorConstraint.notifyValueChange(true);
    }

    public Constraint<Color> useColor() {
        return this.colorConstraint;
    }

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        Shape oldClip = graphics.getClip();
        graphics.setClip(clipShape);

        int x = getX(), y = getY();
        Font font = getFont();
        Color color = getColor();
        String text = getText();

        graphics.setFont(font);
        graphics.setColor(color);
        graphics.drawString(text, x, y);

        graphics.setClip(oldClip);
    }

    public BoundaryRectangle getBoundingBox() {
        if (internalGraphics == null) {
            return new BoundaryRectangle(x, y, -1, -1);
        }
        // The bounding box includes leading
        String text = getText();
        FontMetrics metrics = internalGraphics.getFontMetrics();
        Rectangle2D box = metrics.getStringBounds(text, internalGraphics);

        // Coordinates were relative to the reference point
        int x = getX(), y = getY();
        box.setRect(x + box.getX(), y + box.getY(),
                    box.getWidth(), box.getHeight());
        return new BoundaryRectangle(box);
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
}