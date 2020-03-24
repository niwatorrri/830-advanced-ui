package graphics.object;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import graphics.group.Group;
import constraint.Constraint;
import constraint.NoConstraint;

public class Line implements GraphicalObject {
    /**
     * Line class: lines
     */
    private int x1, y1, x2, y2;
    private Color color;
    private int lineThickness;
    private Group group = null;

    private Constraint<Integer> x1Constraint = new NoConstraint<>();
    private Constraint<Integer> y1Constraint = new NoConstraint<>();
    private Constraint<Integer> x2Constraint = new NoConstraint<>();
    private Constraint<Integer> y2Constraint = new NoConstraint<>();
    private Constraint<Color> colorConstraint = new NoConstraint<>();
    private Constraint<Integer> lineThicknessConstraint = new NoConstraint<>();

    /**
     * Constructors
     */
    public Line(int x1, int y1, int x2, int y2, Color color, int lineThickness) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.lineThickness = lineThickness;
    }

    public Line() {
        this(0, 0, 10, 10, Color.BLACK, 1);
    }

    /**
     * Getters, setters and "users"
     * 
     * Note: user (e.g. useX) returns the constraint on the variable (X)
     */
    public int getX1() {
        if (x1Constraint.isConstrained()) {
            this.x1 = x1Constraint.evaluate();
        }
        return this.x1;
    }

    public void setX1(int x1) {
        if (this.x1 != x1) {
            if (!x1Constraint.isConstrained()) {
                this.x1 = x1;
                x1Constraint.notifyValueChange(false);
            } else if (x1Constraint.hasCycle()) {
                // if no cycle, set a constrained x is no-op
                // if cycle, set local value and do multi-way constraint
                x1Constraint.setValue(x1);
                x1Constraint.notifyValueChange(false);
            }
        }
    }

    public void setX1(Constraint<Integer> constraint) {
        // update dependency graph for the new constraint
        x1Constraint.replaceWithConstraint(constraint);
        x1Constraint = constraint;
        x1Constraint.setValue(this.x1);
        x1Constraint.notifyValueChange(true);
    }

    public Constraint<Integer> useX1() {
        return this.x1Constraint;
    }

    public int getY1() {
        if (y1Constraint.isConstrained()) {
            this.y1 = y1Constraint.evaluate();
        }
        return this.y1;
    }

    public void setY1(int y1) {
        if (this.y1 != y1) {
            if (!y1Constraint.isConstrained()) {
                this.y1 = y1;
                y1Constraint.notifyValueChange(false);
            } else if (y1Constraint.hasCycle()) {
                y1Constraint.setValue(y1);
                y1Constraint.notifyValueChange(false);
            }
        }
    }

    public void setY1(Constraint<Integer> constraint) {
        y1Constraint.replaceWithConstraint(constraint);
        y1Constraint = constraint;
        y1Constraint.setValue(this.y1);
        y1Constraint.notifyValueChange(true);
    }

    public Constraint<Integer> useY1() {
        return this.y1Constraint;
    }

    public int getX2() {
        if (x2Constraint.isConstrained()) {
            this.x2 = x2Constraint.evaluate();
        }
        return this.x2;
    }

    public void setX2(int x2) {
        if (this.x2 != x2) {
            if (!x2Constraint.isConstrained()) {
                this.x2 = x2;
                x2Constraint.notifyValueChange(false);
            } else if (x2Constraint.hasCycle()) {
                // if no cycle, set a constrained x is no-op
                // if cycle, set local value and do multi-way constraint
                x2Constraint.setValue(x2);
                x2Constraint.notifyValueChange(false);
            }
        }
    }

    public void setX2(Constraint<Integer> constraint) {
        // update dependency graph for the new constraint
        x2Constraint.replaceWithConstraint(constraint);
        x2Constraint = constraint;
        x2Constraint.setValue(this.x2);
        x2Constraint.notifyValueChange(true);
    }

    public Constraint<Integer> useX2() {
        return this.x2Constraint;
    }

    public int getY2() {
        if (y2Constraint.isConstrained()) {
            this.y2 = y2Constraint.evaluate();
        }
        return this.y2;
    }

    public void setY2(int y2) {
        if (this.y2 != y2) {
            if (!y2Constraint.isConstrained()) {
                this.y2 = y2;
                y2Constraint.notifyValueChange(false);
            } else if (y2Constraint.hasCycle()) {
                y2Constraint.setValue(y2);
                y2Constraint.notifyValueChange(false);
            }
        }
    }

    public void setY2(Constraint<Integer> constraint) {
        y2Constraint.replaceWithConstraint(constraint);
        y2Constraint = constraint;
        y2Constraint.setValue(this.y2);
        y2Constraint.notifyValueChange(true);
    }

    public Constraint<Integer> useY2() {
        return this.y2Constraint;
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

    public int getLineThickness() {
        if (lineThicknessConstraint.isConstrained()) {
            this.lineThickness = lineThicknessConstraint.evaluate();
        }
        return this.lineThickness;
    }

    public void setLineThickness(int lineThickness) {
        if (this.lineThickness != lineThickness) {
            if (!lineThicknessConstraint.isConstrained()) {
                this.lineThickness = lineThickness;
                lineThicknessConstraint.notifyValueChange(false);
            } else if (lineThicknessConstraint.hasCycle()) {
                lineThicknessConstraint.setValue(lineThickness);
                lineThicknessConstraint.notifyValueChange(false);
            }
        }
    }

    public void setLineThickness(Constraint<Integer> constraint) {
        lineThicknessConstraint.replaceWithConstraint(constraint);
        lineThicknessConstraint = constraint;
        lineThicknessConstraint.setValue(this.lineThickness);
        lineThicknessConstraint.notifyValueChange(true);
    }

    public Constraint<Integer> useLineThickness() {
        return this.lineThicknessConstraint;
    }

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        Shape oldClip = graphics.getClip();
        graphics.setClip(clipShape);

        int x1 = getX1(), y1 = getY1(), x2 = getX2(), y2 = getY2();
        int lineThickness = getLineThickness();
        Color color = getColor();

        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(
            (float)lineThickness,
            BasicStroke.CAP_BUTT,   // end cap style
            BasicStroke.JOIN_ROUND  // line join style
        ));
        graphics.drawLine(x1, y1, x2, y2);
        
        graphics.setClip(oldClip);
    }

    public BoundaryRectangle getBoundingBox() {
        // A relaxed bounding box
        int x1 = getX1(), y1 = getY1(), x2 = getX2(), y2 = getY2();
        int lineThickness = getLineThickness();

        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);
        double sinTheta = dy / Math.sqrt(dx * dx + dy * dy);
        double cosTheta = dx / Math.sqrt(dx * dx + dy * dy);
        return new BoundaryRectangle(
            Math.min(x1, x2) - lineThickness * sinTheta / 2,
            Math.min(y1, y2) - lineThickness * cosTheta / 2,
            dx + lineThickness * sinTheta,
            dy + lineThickness * cosTheta
        );
    }

    public void moveTo(int x, int y) {
        BoundaryRectangle boundingBox = getBoundingBox();
        int topLeftX = boundingBox.x;
        int topLeftY = boundingBox.y;

        int x1 = getX1(), y1 = getY1();
        int x2 = getX2(), y2 = getY2();
        this.setX1(x1 + x - topLeftX);
        this.setX2(x2 + x - topLeftX);
        this.setY1(y1 + y - topLeftY);
        this.setY2(y2 + y - topLeftY);
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