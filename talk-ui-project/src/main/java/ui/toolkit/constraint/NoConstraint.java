package ui.toolkit.constraint;

public class NoConstraint<T> extends Constraint<T> {
    /**
     * NoConstraint class: a Constraint object with no constraint
     */
    public NoConstraint() {
        super();
    }
    public T getValue() {
        return this.value;
    }
}