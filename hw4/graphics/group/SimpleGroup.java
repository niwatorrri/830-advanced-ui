package graphics.group;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ArrayList;

import graphics.object.GraphicalObject;
import graphics.object.BoundaryRectangle;
import graphics.object.AlreadyHasGroupRunTimeException;
import constraint.Constraint;

public class SimpleGroup implements Group {
    /**
     * SimpleGroup class: a group of objects at their fixed positions
     */
    private int x, y, width, height;
    private Group group = null;
    private List<GraphicalObject> children = new ArrayList<>();

    private Constraint<Integer> xConstraint = new Constraint<>();
    private Constraint<Integer> yConstraint = new Constraint<>();
    private Constraint<Integer> widthConstraint = new Constraint<>();
    private Constraint<Integer> heightConstraint = new Constraint<>();

    /**
     * Constructors
     */
    public SimpleGroup(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public SimpleGroup() {
        this(0, 0, 200, 200);
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

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        // Intersect the clip shape with the group bounding box
        Shape commonClipArea = getBoundingBox().intersection(clipShape.getBounds());

        // Translate the new clip shape to pass to children
        int x = getX(), y = getY();
        AffineTransform transform = new AffineTransform();
        transform.translate(-x, -y);
        Shape childClipShape = transform.createTransformedShape(commonClipArea);

        // Translate the origin to draw children
        graphics.translate(x, y);
        for (GraphicalObject child: children) {
            child.draw(graphics, childClipShape);
        }
        graphics.translate(-x, -y);
    }

    public BoundaryRectangle getBoundingBox() {
        int x = getX(), y = getY(), width = getWidth(), height = getHeight();
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
        for (GraphicalObject child: children) {
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
        int childX = pt.x - x;
        int childY = pt.y - y;
        return new Point(childX, childY);
    }

    public Point childToParent(Point pt) {
        int x = getX(), y = getY();
        int parentX = pt.x + x;
        int parentY = pt.y + y;
        return new Point(parentX, parentY);
    }
}