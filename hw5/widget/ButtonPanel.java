package widget;

import java.awt.Color;
import java.util.List;

import behavior.ChoiceBehavior;
import constraint.Constraint;
import constraint.NoConstraint;
import graphics.group.LayoutGroup;
import graphics.group.SimpleGroup;
import graphics.object.GraphicalObject;
import graphics.object.selectable.SelectableFilledRect;

public class ButtonPanel extends Widget {
    private boolean finalFeedback;

    private List<Button> value;
    private Constraint<List<Button>> valueConstraint = new NoConstraint<>();

    public static final int SINGLE = ChoiceBehavior.SINGLE;
    public static final int MULTIPLE = ChoiceBehavior.MULTIPLE;

    public ButtonPanel(int x, int y, boolean finalFeedback, int selectionType, int layout, int offset) {
        if (layout == NO_LAYOUT) {
            this.widget = new SimpleGroup(x, y, 0, 0);
        } else {
            this.widget = new LayoutGroup(x, y, 0, 0, layout, offset);
        }
        this.widget.addBehavior(new ChoiceBehavior(selectionType, false));
        this.finalFeedback = finalFeedback;
    }

    public ButtonPanel(int x, int y, boolean finalFeedback, int selectionType) {
        this(x, y, finalFeedback, selectionType, VERTICAL_LAYOUT, 10);
    }

    public ButtonPanel() {
        this(0, 0, true, SINGLE);
    }

    @Override
    public Widget addChild(GraphicalObject child) {
        super.addChild(child);

        SelectableFilledRect r = ((Button) child).getBox();
        r.setColor(new Constraint<Color>(r.useInterimSelected(), r.useSelected()) {
            public Color getValue() {
                if (r.isSelected() && finalFeedback) {
                    return r.isInterimSelected() ? Color.GREEN.darker() : Color.GREEN;
                } else {
                    return r.isInterimSelected() ? Color.LIGHT_GRAY.darker() : Color.LIGHT_GRAY;
                }
            }
        });
        return this;
    }

    /**
     * Getter, setter and "user" for value
     */
    public List<Button> getValue() {
        if (valueConstraint.isConstrained()) {
            this.value = valueConstraint.evaluate();
        }
        return this.value;
    }

    public void setValue(List<Button> value) {
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

    public void setValue(Constraint<List<Button>> constraint) {
        valueConstraint.replaceWithConstraint(constraint);
        valueConstraint = constraint;
        valueConstraint.setValue(this.value);
        valueConstraint.notifyValueChange(true);
    }

    public Constraint<List<Button>> useValue() {
        return this.valueConstraint;
    }
}