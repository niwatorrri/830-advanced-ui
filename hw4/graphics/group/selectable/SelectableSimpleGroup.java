package graphics.group.selectable;

import graphics.group.SimpleGroup;
import constraint.Constraint;

public class SelectableSimpleGroup extends SimpleGroup implements SelectableGroup {
    /**
     * SelectableSimpleGroup class: a selectable group with objects at their fixed positions
     */
    private boolean interimSelected;
    private boolean selected;

    private Constraint<Boolean> interimSelectedConstraint = new Constraint<>();
    private Constraint<Boolean> selectedConstraint = new Constraint<>();

    /**
     * Constructors
     */
    public SelectableSimpleGroup(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public SelectableSimpleGroup() {
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
        interimSelectedConstraint.updateConstraint(constraint);
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
        selectedConstraint.updateConstraint(constraint);
        selectedConstraint = constraint;
        selectedConstraint.setValue(this.selected);
        selectedConstraint.notifyValueChange(true);
    }

    public Constraint<Boolean> useSelected() {
        return this.selectedConstraint;
    }
}