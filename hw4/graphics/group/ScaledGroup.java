package graphics.group;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ArrayList;

import graphics.object.GraphicalObject;
import graphics.object.BoundaryRectangle;
import graphics.object.AlreadyHasGroupRunTimeException;
import constraint.Constraint;

public class ScaledGroup implements Group {
    /**
     * ScaledGroup class
     * 
     * Shrinks or enlarges a group of graphical objects by a scale factor
     */
    private int x, y, width, height;
    private double scaleX, scaleY;
    private Group group = null;
    private List<GraphicalObject> children = new ArrayList<>();

    private Constraint<Integer> xConstraint = new Constraint<>();
    private Constraint<Integer> yConstraint = new Constraint<>();
    private Constraint<Integer> widthConstraint = new Constraint<>();
    private Constraint<Integer> heightConstraint = new Constraint<>();
    private Constraint<Double> scaleXConstraint = new Constraint<>();
    private Constraint<Double> scaleYConstraint = new Constraint<>();

    /**
     * Constructors
     */
    public ScaledGroup(int x, int y, int width, int height, double scaleX, double scaleY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public ScaledGroup() {
        this(0, 0, 200, 200, 1.0, 1.0);
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

    public int getWidth() {
        if (widthConstraint.isConstrained()) {
            this.width = widthConstraint.evaluate();
        }
        return this.width;
    }

    public void setWidth(int width) {
        if (this.width != width) {
            if (!widthConstraint.isConstrained()) {
                this.width = width;
                widthConstraint.notifyValueChange(false);
            } else if (widthConstraint.hasCycle()) {
                widthConstraint.setValue(width);
                widthConstraint.notifyValueChange(false);
            }
        }
    }

    public void setWidth(Constraint<Integer> constraint) {
        widthConstraint.updateConstraint(constraint);
        widthConstraint = constraint;
        widthConstraint.setValue(this.width);
        widthConstraint.notifyValueChange(true);
    }

    public Constraint<Integer> useWidth() {
        return this.widthConstraint;
    }

    public int getHeight() {
        if (heightConstraint.isConstrained()) {
            this.height = heightConstraint.evaluate();
        }
        return this.height;
    }

    public void setHeight(int height) {
        if (this.height != height) {
            if (!heightConstraint.isConstrained()) {
                this.height = height;
                heightConstraint.notifyValueChange(false);
            } else if (heightConstraint.hasCycle()) {
                heightConstraint.setValue(height);
                heightConstraint.notifyValueChange(false);
            }
        }
    }

    public void setHeight(Constraint<Integer> constraint) {
        heightConstraint.updateConstraint(constraint);
        heightConstraint = constraint;
        heightConstraint.setValue(this.height);
        heightConstraint.notifyValueChange(true);
    }

    public Constraint<Integer> useHeight() {
        return this.heightConstraint;
    }

    public double getScaleX() {
        if (scaleXConstraint.isConstrained()) {
            this.scaleX = scaleXConstraint.evaluate();
        }
        return this.scaleX;
    }

    public void setScaleX(double scaleX) {
        if (this.scaleX != scaleX) {
            if (!scaleXConstraint.isConstrained()) {
                this.scaleX = scaleX;
                scaleXConstraint.notifyValueChange(false);
            } else if (scaleXConstraint.hasCycle()) {
                scaleXConstraint.setValue(scaleX);
                scaleXConstraint.notifyValueChange(false);
            }
        }
    }

    public void setScaleX(Constraint<Double> constraint) {
        scaleXConstraint.updateConstraint(constraint);
        scaleXConstraint = constraint;
        scaleXConstraint.setValue(this.scaleX);
        scaleXConstraint.notifyValueChange(true);
    }

    public Constraint<Double> useScaleX() {
        return this.scaleXConstraint;
    }

    public double getScaleY() {
        if (scaleYConstraint.isConstrained()) {
            this.scaleY = scaleYConstraint.evaluate();
        }
        return this.scaleY;
    }

    public void setScaleY(double scaleY) {
        if (this.scaleY != scaleY) {
            if (!scaleYConstraint.isConstrained()) {
                this.scaleY = scaleY;
                scaleYConstraint.notifyValueChange(false);
            } else if (scaleYConstraint.hasCycle()) {
                scaleYConstraint.setValue(scaleY);
                scaleYConstraint.notifyValueChange(false);
            }
        }
    }

    public void setScaleY(Constraint<Double> constraint) {
        scaleYConstraint.updateConstraint(constraint);
        scaleYConstraint = constraint;
        scaleYConstraint.setValue(this.scaleY);
        scaleYConstraint.notifyValueChange(true);
    }

    public Constraint<Double> useScaleY() {
        return this.scaleYConstraint;
    }

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        // Turn on anti-aliasing for aesthetics
        RenderingHints oldRenderingHints = graphics.getRenderingHints();
        graphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Intersect the clip shape with the group bounding box
        Shape commonClipArea = getBoundingBox().intersection(clipShape.getBounds());

        // Transform the new clip shape to pass to children
        // Note: transforms are right-associative
        int x = getX(), y = getY();
        double scaleX = getScaleX(), scaleY = getScaleY();
        AffineTransform shapeTransform = new AffineTransform();
        shapeTransform.scale(1.0 / scaleX, 1.0 / scaleY);
        shapeTransform.translate(-x, -y);
        Shape childClipShape = shapeTransform.createTransformedShape(commonClipArea);

        // Transform the graphics to draw children
        AffineTransform oldTransform = graphics.getTransform();
        graphics.translate(x, y);           // 1. translate the origin
        graphics.scale(scaleX, scaleY);     // 2. scale the graphics
        for (GraphicalObject child : children) {
            child.draw(graphics, childClipShape);
        }

        // Restore old graphical attributes
        graphics.setTransform(oldTransform);
        graphics.setRenderingHints(oldRenderingHints);
    }

    public BoundaryRectangle getBoundingBox() {
        int x = getX(), y = getY(), width = getWidth(), height = getHeight();
        double scaleX = getScaleX(), scaleY = getScaleY();

        // Relaxed a little bit to count for floating point error
        return new BoundaryRectangle(x, y, width * scaleX, height * scaleY);
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

    /**
     * Methods defined in the Group interface
     */
    public void addChild(GraphicalObject child) {
        Group childGroup = child.getGroup();
        if (childGroup != null) {
            throw new AlreadyHasGroupRunTimeException();
        } else {
            children.add(child);
            child.setGroup(this);
        }
    }

    public void removeChild(GraphicalObject child) {
        children.remove(child);
        child.setGroup(null);
    }

    public void bringChildToFront(GraphicalObject child) {
        if (children.remove(child)) {
            children.add(child);
        } else {
            throw new RuntimeException("Object is not in the group");
        }
    }

    public void resizeToChildren() {
        int newWidth = 0, newHeight = 0;
        for (GraphicalObject child : children) {
            BoundaryRectangle box = child.getBoundingBox();
            newWidth = Math.max(newWidth, (int) box.getMaxX());
            newHeight = Math.max(newHeight, (int) box.getMaxY());
        }
        this.setWidth(newWidth);
        this.setHeight(newHeight);
    }

    public List<GraphicalObject> getChildren() {
        return new ArrayList<GraphicalObject>(children);
    }

    public Point parentToChild(Point pt) {
        int x = getX(), y = getY();
        double scaleX = getScaleX(), scaleY = getScaleY();

        int childX = (int) ((pt.x - x) / scaleX);
        int childY = (int) ((pt.y - y) / scaleY);
        return new Point(childX, childY);
    }

    public Point childToParent(Point pt) {
        int x = getX(), y = getY();
        double scaleX = getScaleX(), scaleY = getScaleY();

        int parentX = (int) (pt.x * scaleX + x);
        int parentY = (int) (pt.y * scaleY + y);
        return new Point(parentX, parentY);
    }
}