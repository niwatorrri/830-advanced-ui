import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Text implements GraphicalObject {
    private Graphics internalGraphics; // for calculation only
    private String text;
    private int x, y;
    private Font font;
    private Color color;
    private Group group = null;

    private static final Font defaultFont
        = new Font("Monospaced", Font.PLAIN, 10);

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
        this(null, "Text", 10, 10, defaultFont, Color.BLACK);
    }

    /**
     * Getters and setters
     */
    public Graphics getGraphics() {
        return this.internalGraphics;
    }

    public void setGraphics(Graphics graphics) {
        this.internalGraphics = graphics;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
        internalGraphics.setFont(font);
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
        internalGraphics.setColor(color);
    }
    
    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        Shape oldClip = graphics.getClip();
        graphics.setClip(clipShape);

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
        FontMetrics metrics = internalGraphics.getFontMetrics();
        Rectangle2D box = metrics.getStringBounds(text, internalGraphics);

        // Coordinates were relative to the reference point
        box.setRect(x + box.getX(), y + box.getY(),
                    box.getWidth(), box.getHeight());
        return new BoundaryRectangle(box);
    }

    public void moveTo(int x, int y) {
        BoundaryRectangle boundingBox = getBoundingBox();
        int topLeftX = boundingBox.x;
        int topLeftY = boundingBox.y;
        this.x += x - topLeftX;
        this.y += y - topLeftY;
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean contains(int x, int y) {
        return getBoundingBox().contains(x, y);
    }
}