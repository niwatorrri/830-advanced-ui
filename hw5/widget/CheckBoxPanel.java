package widget;

import java.awt.Color;
import java.util.List;

import behavior.ChoiceBehavior;
import constraint.Constraint;
import constraint.NoConstraint;
import graphics.group.LayoutGroup;
import graphics.group.SimpleGroup;
import graphics.object.GraphicalObject;
import graphics.object.Text;
import graphics.object.selectable.SelectableOutlineRect;

public class CheckBoxPanel extends Widget {
    private List<CheckBox> value;
    private Constraint<List<CheckBox>> valueConstraint = new NoConstraint<>();

    public CheckBoxPanel(int x, int y, int layout, int offset) {
        if (layout == NO_LAYOUT) {
            this.widget = new SimpleGroup(x, y, 0, 0);
        } else {
            this.widget = new LayoutGroup(x, y, 0, 0, layout, offset);
        }
        this.widget.addBehavior(new ChoiceBehavior(ChoiceBehavior.MULTIPLE, true));
    }

    public CheckBoxPanel(int x, int y) {
        this(x, y, VERTICAL_LAYOUT, 5);
    }

    public CheckBoxPanel() {
        this(0, 0);
    }

    @Override
    public Widget addChild(GraphicalObject child) {
        super.addChild(child);

        SelectableOutlineRect o = ((CheckBox) child).getOption();
        Text i = ((CheckBox) child).getIndicator();
        i.setColor(new Constraint<Color>(o.useInterimSelected(), o.useSelected()) {
            public Color getValue() {
                if (o.isSelected()) {
                    return o.isInterimSelected() ? Color.LIGHT_GRAY : Color.BLUE;
                } else {
                    return o.isInterimSelected() ? Color.LIGHT_GRAY : Color.WHITE;
                }
            }
        });
        return this;
    }

    /**
     * Getter, setter and "user" for value
     */
    public List<CheckBox> getValue() {
        if (valueConstraint.isConstrained()) {
            this.value = valueConstraint.evaluate();
        }
        return this.value;
    }

    public void setValue(List<CheckBox> value) {
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

    public void setValue(Constraint<List<CheckBox>> constraint) {
        valueConstraint.replaceWithConstraint(constraint);
        valueConstraint = constraint;
        valueConstraint.setValue(this.value);
        valueConstraint.notifyValueChange(true);
    }

    public Constraint<List<CheckBox>> useValue() {
        return this.valueConstraint;
    }
}