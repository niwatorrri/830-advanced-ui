package widget;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.util.List;

import behavior.Behavior;
import constraint.Constraint;
import constraint.NoConstraint;
import graphics.group.Group;
import graphics.group.LayoutGroup;
import graphics.object.BoundaryRectangle;
import graphics.object.GraphicalObject;

public class Widget<T> implements Group {
    protected Group widget;

    protected T value = null;
    protected Constraint<T> valueConstraint = new NoConstraint<>();

    public static final int NO_LAYOUT = -1;
    public static final int HORIZONTAL_LAYOUT = LayoutGroup.HORIZONTAL;
    public static final int VERTICAL_LAYOUT = LayoutGroup.VERTICAL;
    public static final int GRID_LAYOUT = LayoutGroup.GRID;

    /**
     * Callbacks: methods to call when value changes
     */
    protected Callback<T> callback = v -> {};

    public Widget<T> setCallback(Callback<T> callback) {
        this.callback = callback;
        return this;
    }

    /**
     * Getter, setter and "user" for value
     */
    public T getValue() {
        if (valueConstraint.isConstrained()) {
            this.value = valueConstraint.evaluate();
        }
        return this.value;
    }

    public void setValue(T value) {
        boolean valuesEqual = (this.value == null || value == null) ?
                (this.value == value) : (this.value.equals(value));
        if (!valuesEqual) {
            if (!valueConstraint.isConstrained()) {
                this.value = value;
                valueConstraint.notifyValueChange(false);
            } else if (valueConstraint.hasCycle()) {
                valueConstraint.setValue(value);
                valueConstraint.notifyValueChange(false);
            }
        }
    }

    public void setValue(Constraint<T> constraint) {
        valueConstraint.replaceWithConstraint(constraint);
        valueConstraint = constraint;
        valueConstraint.setValue(this.value);
        valueConstraint.notifyValueChange(true);
    }

    public Constraint<T> useValue() {
        return this.valueConstraint;
    }

    /**
     * Methods defined in the Group interface
     */
    public Widget<T> addChild(GraphicalObject child) {
        widget.addChild(child).resizeToChildren();
        return this;
    }

    public Widget<T> addChildren(GraphicalObject... children) {
        for (GraphicalObject child : children) {
            this.addChild(child);
        }
        return this;
    }

    public Widget<T> removeChild(GraphicalObject child) {
        widget.removeChild(child);
        return this;
    }

    public Widget<T> removeChildren(GraphicalObject... children) {
        for (GraphicalObject child : children) {
            this.removeChild(child);
        }
        return this;
    }

    public Widget<T> addBehavior(Behavior behavior) {
        widget.addBehavior(behavior);
        return this;
    }

    public Widget<T> addBehaviors(Behavior... behaviors) {
        widget.addBehaviors(behaviors);
        return this;
    }

    public Widget<T> removeBehavior(Behavior behavior) {
        widget.removeBehavior(behavior);
        return this;
    }

    public Widget<T> removeBehaviors(Behavior... behaviors) {
        widget.removeBehaviors(behaviors);
        return this;
    }

    public Widget<T> clearBehavior() {
        for (Behavior behavior : widget.getBehaviors()) {
            widget.removeBehavior(behavior);
        }
        return this;
    }

    public List<Behavior> getBehaviors() {
        return widget.getBehaviors();
    }

    public Widget<T> bringChildToFront(GraphicalObject child) {
        widget.bringChildToFront(child);
        return this;
    }

    public Widget<T> resizeToChildren() {
        widget.resizeToChildren();
        return this;
    }

    public List<GraphicalObject> getChildren() {
        return widget.getChildren();
    }

    public Point parentToChild(Point pt) {
        return widget.parentToChild(pt);
    }

    public Point childToParent(Point pt) {
        return widget.childToParent(pt);
    }

    public Behavior[] getBehaviorsToAdd() {
        return widget.getBehaviorsToAdd();
    }

    public Behavior[] getBehaviorsToRemove() {
        return widget.getBehaviorsToRemove();
    }

    public Group clearBehaviorsToAdd() {
        return widget.clearBehaviorsToAdd();
    }

    public Group clearBehaviorsToRemove() {
        return widget.clearBehaviorsToRemove();
    }

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        widget.draw(graphics, clipShape);
    }

    public BoundaryRectangle getBoundingBox() {
        return widget.getBoundingBox();
    }

    public void moveTo(int x, int y) {
        widget.moveTo(x, y);
    }

    public Group getGroup() {
        return widget.getGroup();
    }

    public void setGroup(Group group) {
        widget.setGroup(group);
    }

    public boolean contains(int x, int y) {
        return widget.contains(x, y);
    }

    public boolean contains(Point pt) {
        return widget.contains(pt);
    }

    public void setX(Constraint<Integer> constraint) {
        widget.setX(constraint);
    }

    public void setY(Constraint<Integer> constraint) {
        widget.setY(constraint);
    }
}