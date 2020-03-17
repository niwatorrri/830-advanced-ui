package graphics.object;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;

import graphics.group.Group;
import constraint.Constraint;

public class Icon implements GraphicalObject {
    /**
     * Icon class: images
     */
    private Image image;
    private int x, y;
    private Group group = null;

    private Constraint<Image> imageConstraint = new Constraint<>();
    private Constraint<Integer> xConstraint = new Constraint<>();
    private Constraint<Integer> yConstraint = new Constraint<>();

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

    public Image getImage() {
        if (imageConstraint.isConstrained()) {
            this.image = imageConstraint.evaluate();
        }
        return this.image;
    }

    public void setImage(Image image) {
        if (this.image != image) {
            if (!imageConstraint.isConstrained()) {
                this.image = image;
                imageConstraint.notifyValueChange(false);
            } else if (imageConstraint.hasCycle()) {
                imageConstraint.setValue(image);
                imageConstraint.notifyValueChange(false);
            }
        }
    }

    public void setImage(Constraint<Image> constraint) {
        imageConstraint.updateConstraint(constraint);
        imageConstraint = constraint;
        imageConstraint.setValue(this.image);
        imageConstraint.notifyValueChange(true);
    }

    public Constraint<Image> useImage() {
        return this.imageConstraint;
    }

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        Image image = getImage();
        if (image == null) {
            return;
        }
        int x = getX(), y = getY();
        Shape oldClip = graphics.getClip();
        graphics.setClip(clipShape);
        graphics.drawImage(image, x, y, null);
        graphics.setClip(oldClip);
    }

    public BoundaryRectangle getBoundingBox() {
        Image image = getImage();
        if (image == null) { // mimics java.awt.Image behavior
            return new BoundaryRectangle(x, y, -1, -1);
        }
        int x = getX(), y = getY();
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        return new BoundaryRectangle(x, y, width, height);
    }

    public void moveTo(int x, int y) {
        this.setX(x);
        this.setY(y);
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