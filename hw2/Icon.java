import java.awt.*;

public class Icon implements GraphicalObject {
    private Image image;
    private int x, y;
    private Group group = null;

    /**
     * Constructors
     */
    public Icon(Image image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public Icon() {
        this(null, 0, 0);
    }

    /**
     * Getters and setters
     */
    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
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

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        if (image == null) {
            return;
        }
        Shape oldClip = graphics.getClip();
        graphics.setClip(clipShape);
        graphics.drawImage(image, x, y, null);
        graphics.setClip(oldClip);
    }

    public BoundaryRectangle getBoundingBox() {
        if (image == null) { // mimics java.awt.Image behavior
            return new BoundaryRectangle(x, y, -1, -1);
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        return new BoundaryRectangle(x, y, width, height);
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        if (this.group != null) {
            throw new AlreadyHasGroupRunTimeException();
        }
        this.group = group;
    }

    public boolean contains(int x, int y) {
        return getBoundingBox().contains(x, y);
    }
}