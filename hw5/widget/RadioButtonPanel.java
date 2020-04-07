package widget;

import java.awt.Color;
import java.util.List;

import behavior.ChoiceBehavior;
import constraint.Constraint;
import constraint.NoConstraint;
import graphics.group.LayoutGroup;
import graphics.group.SimpleGroup;
import graphics.object.FilledEllipse;
import graphics.object.GraphicalObject;
import graphics.object.selectable.SelectableEllipse;

public class RadioButtonPanel extends Widget {
    private List<RadioButton> value;
    private Constraint<List<RadioButton>> valueConstraint = new NoConstraint<>();

    public RadioButtonPanel(int x, int y, int layout, int offset) {
        if (layout == NO_LAYOUT) {
            this.widget = new SimpleGroup(x, y, 0, 0);
        } else {
            this.widget = new LayoutGroup(x, y, 0, 0, layout, offset);
        }
        this.widget.addBehavior(new ChoiceBehavior(ChoiceBehavior.SINGLE, true));
    }

    public RadioButtonPanel(int x, int y) {
        this(x, y, VERTICAL_LAYOUT, 10);
    }

    public RadioButtonPanel() {
        this(0, 0);
    }

    public Widget addChild(GraphicalObject child) {
        super.addChild(child);

        SelectableEllipse o = ((RadioButton) child).getOption();
        FilledEllipse i = ((RadioButton) child).getIndicator();
        i.setColor(new Constraint<Color>(o.useInterimSelected(), o.useSelected()) {
            public Color getValue() {
                if (o.isSelected()) {
                    return o.isInterimSelected() ? Color.BLUE : Color.BLACK;
                } else {
                    return o.isInterimSelected() ? Color.LIGHT_GRAY : Color.WHITE;
                }
            }
        });
        return this;
    }

    public Widget addChildren(GraphicalObject... children) {
        for (GraphicalObject child : children) {
            addChild(child);
        }
        return this;
    }

    /**
     * Getter, setter and "user" for value
     */
    public List<RadioButton> getValue() {
        if (valueConstraint.isConstrained()) {
            this.value = valueConstraint.evaluate();
        }
        return this.value;
    }

    public void setValue(List<RadioButton> value) {
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

    public void setValue(Constraint<List<RadioButton>> constraint) {
        valueConstraint.replaceWithConstraint(constraint);
        valueConstraint = constraint;
        valueConstraint.setValue(this.value);
        valueConstraint.notifyValueChange(true);
    }

    public Constraint<List<RadioButton>> useValue() {
        return this.valueConstraint;
    }
}