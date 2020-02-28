import java.awt.*;
import java.util.List;
import java.util.ArrayList;

// import java.lang.reflect.Field;

public class OutlineRect implements GraphicalObject {
    private int x, y, width, height;
    private Color color;
    private int lineThickness;
    private Group group = null;

    // TODO: allow multiple constraints on a member
    public Dependency<Integer> xConstraint = new Dependency<>();

    // now keep track of which constraints use my values. This might alternatively
    // go in an object for the attribute itself.
    // private List<Dependency<?>> xOutConstraints= new ArrayList<>();

    /**
     * Constructors
     */
    public OutlineRect(int x, int y, int width, int height,
            Color color, int lineThickness) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.lineThickness = lineThickness;

        // this.xConstraint = new Dependency<>(x);
    }

    public OutlineRect() {
        this(0, 0, 10, 10, Color.BLACK, 1);
    }

    /**
     * Getters and setters
     */
    public int getX() {
        System.out.println(this);
        System.out.println(this.xConstraint.value);
        if (xConstraint.isConstrained()) {
            System.out.println("before evaluate");
            this.x = xConstraint.evaluate();
        }
        return this.x;
    }

    private void notifyValueChange(Dependency<?> constraint) {
        for (Edge outEdge: constraint.outEdges) {
            // Dependency foundDependency = outConstraint.findDependency(this, attribute);
            // if (foundDependency != null) {
            //     foundDependency.markOutOfDate();
            // }
            System.out.println("one notified");
            outEdge.setPending(true);
            outEdge.object.markOutOfDate();
        }
    }

    public void setX(int x) {
        if (this.x != x) {
            this.x = x;
            // if (xConstraint != null) { // TODO: ?
            //     xConstraint.setValue(x);
            // }
            notifyValueChange(xConstraint);
        }
    }

    public void setX(Dependency<Integer> constraint) {
        /*
         * first probably need to check if there was already a constraint there and
         * clean up
         */
        xConstraint = constraint;
        System.out.println("before notify");
        /* might need to do something so the constraint is set up properly */
        /* need to get the value of x somehow */
        // now tell others my value has changed
        // notifyValueChange(xConstraint);
        xConstraint.markOutOfDate();
        // notifyValueChange(xConstraint);
        System.out.println("After notify");
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getLineThickness() {
        return this.lineThickness;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        Shape oldClip = graphics.getClip();
        graphics.setClip(clipShape);

        int x = getX(), y = getY();
        // TODO: more to come

        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(lineThickness));
        graphics.drawRect(
            x + lineThickness / 2,
            y + lineThickness / 2,
            width - lineThickness,
            height - lineThickness
        );
        graphics.setClip(oldClip);
    }

    public BoundaryRectangle getBoundingBox() {
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
}