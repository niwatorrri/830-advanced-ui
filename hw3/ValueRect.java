import java.awt.Color;

import graphics.object.OutlineRect;
import constraint.Constraint;

public class ValueRect extends OutlineRect {
    /**
     * ValueRect class: outline rectangles with values
     * 
     * Subclass example
     */
    private int value;

    private Constraint<Integer> valueConstraint = new Constraint<>();

    /**
     * Constructors
     */
    public ValueRect(int x, int y, int width, int height,
            Color color, int lineThickness, int value) {
        super(x, y, width, height, color, lineThickness);
        this.value = value;
    }

    public ValueRect() {
        this(0, 0, 10, 10, Color.BLACK, 1, 0);
    }

    /**
     * Getters, setters and "users"
     * 
     * Maybe should use Java reflection API to make writing subclasses easier.
     * However, reflection API can lead to performance overhead. Basically
     * subclasses only need to change the corresponding member names and
     * constraint names.
     */
    public int getValue() {
        if (valueConstraint.isConstrained()) {
            this.value = valueConstraint.evaluate();
        }
        return this.value;
    }

    public void setValue(int value) {
        if (this.value != value) {
            if (!valueConstraint.isConstrained()) {
                this.value = value;
                valueConstraint.notifyValueChange(false);
            } else if (valueConstraint.hasCycle()) {
                valueConstraint.setValue(value);
                valueConstraint.notifyValueChange(false);
            }
        }
    }

    public void setValue(Constraint<Integer> constraint) {
        valueConstraint.updateConstraint(constraint);
        valueConstraint = constraint;
        valueConstraint.setValue(this.value);
        valueConstraint.notifyValueChange(true);
    }

    public Constraint<Integer> useValue() {
        return this.valueConstraint;
    }
}