import java.awt.*;

public class OutlineRect implements GraphicalObject {
    private int x, y, width, height;
    private Color color;
    private int lineThickness;
    private Group group = null;

    // TODO: allow multiple constraints on a member
    private Constraint<Integer> xConstraint = new Constraint<>();

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
    }

    public OutlineRect() {
        this(0, 0, 10, 10, Color.BLACK, 1);
    }

    /**
     * Getters, setters and "users"
     * Note: user (useX) returns the constraint on the variable (X)
     */
    public int getX() {
        if (xConstraint.isConstrained()) {
            this.x = xConstraint.evaluate();
        }
        return this.x;
    }

    private void notifyValueChange(Constraint<?> constraint, boolean selfOutOfDate) {
        constraint.setOutOfDate(selfOutOfDate);
        for (Edge outEdge: constraint.getOutEdges()) {
            outEdge.setPending(true);
            outEdge.markOutOfDate();
        }
    }

    public void setX(int x) {
        // TODO: remove original constraint or no-op?
        // set the local value and cause  invalidating in a multi-way constraint system
        // if (xConstraint != null) {
        //     xConstraint.setValue(x);
        // }
        if (this.x != x) {
            this.x = x;
            notifyValueChange(xConstraint, false);
        }
    }

    public void setX(Constraint<Integer> constraint) {
        // remove the constraint in a formula constraint system
        /*
         * first need to check if there was already a constraint
         */
        xConstraint.updateConstraint(constraint);
        xConstraint = constraint;
        notifyValueChange(xConstraint, true);
    }

    public Constraint<Integer> useX() {
        return this.xConstraint;
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