package ui.toolkit.graphics.group.selectable;

import ui.toolkit.constraint.Constraint;
import ui.toolkit.constraint.NoConstraint;
import ui.toolkit.graphics.group.ScaledGroup;

public class SelectableScaledGroup extends ScaledGroup implements SelectableGroup {
    /**
     * SelectableScaledGroup class: a selectable ScaledGroup
     * 
     * Shrinks or enlarges a group of graphical objects by a scale factor
     */
    private boolean interimSelected;
    private boolean selected;

    private Constraint<Boolean> interimSelectedConstraint = new NoConstraint<>();
    private Constraint<Boolean> selectedConstraint = new NoConstraint<>();

    /**
     * Constructors
     */
    public SelectableScaledGroup(int x, int y, int width, int height, double scaleX, double scaleY) {
        super(x, y, width, height, scaleX, scaleY);
    }

    public SelectableScaledGroup() {
        super();
    }

    /**
     * Getters, setters and "users"
     */
    public boolean isInterimSelected() {
        if (interimSelectedConstraint.isConstrained()) {
            this.interimSelected = interimSelectedConstraint.evaluate();
        }
        return this.interimSelected;
    }

    public void setInterimSelected(boolean interimSelected) {
        if (this.interimSelected != interimSelected) {
            if (!interimSelectedConstraint.isConstrained()) {
                this.interimSelected = interimSelected;
                interimSelectedConstraint.notifyValueChange(false);
            } else if (interimSelectedConstraint.hasCycle()) {
                interimSelectedConstraint.setValue(interimSelected);
                interimSelectedConstraint.notifyValueChange(false);
            }
        }
    }

    public void setInterimSelected(Constraint<Boolean> constraint) {
        interimSelectedConstraint.replaceWithConstraint(constraint);
        interimSelectedConstraint = constraint;
        interimSelectedConstraint.setValue(this.interimSelected);
        interimSelectedConstraint.notifyValueChange(true);
    }

    public Constraint<Boolean> useInterimSelected() {
        return this.interimSelectedConstraint;
    }

    public boolean isSelected() {
        if (selectedConstraint.isConstrained()) {
            this.selected = selectedConstraint.evaluate();
        }
        return this.selected;
    }

    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            if (!selectedConstraint.isConstrained()) {
                this.selected = selected;
                selectedConstraint.notifyValueChange(false);
            } else if (selectedConstraint.hasCycle()) {
                selectedConstraint.setValue(selected);
                selectedConstraint.notifyValueChange(false);
            }
        }
    }

    public void setSelected(Constraint<Boolean> constraint) {
        selectedConstraint.replaceWithConstraint(constraint);
        selectedConstraint = constraint;
        selectedConstraint.setValue(this.selected);
        selectedConstraint.notifyValueChange(true);
    }

    public Constraint<Boolean> useSelected() {
        return this.selectedConstraint;
    }
}