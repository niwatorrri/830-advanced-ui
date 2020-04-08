package widget;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import behavior.BehaviorEvent;
import behavior.ChoiceBehavior;
import constraint.Constraint;
import graphics.group.LayoutGroup;
import graphics.group.SimpleGroup;
import graphics.object.GraphicalObject;
import graphics.object.Text;
import graphics.object.selectable.SelectableOutlineRect;

public class CheckBoxPanel extends Widget<List<CheckBox>> {
    /**
     * CheckBoxPanel constructor
     * 
     * @param x
     * @param y
     * @param layout
     * @param offset
     */
    public CheckBoxPanel(int x, int y, int layout, int offset) {
        if (layout == NO_LAYOUT) {
            this.widget = new SimpleGroup(x, y, 0, 0);
        } else {
            this.widget = new LayoutGroup(x, y, 0, 0, layout, offset);
        }

        this.value = new ArrayList<CheckBox>();
        this.widget.addBehavior(
            new ChoiceBehavior(ChoiceBehavior.MULTIPLE, true) {
                @Override
                @SuppressWarnings("unchecked")
                public boolean stop(BehaviorEvent event) {
                    boolean eventConsumed = super.stop(event);
                    List<CheckBox> selection = (List<CheckBox>) (List<?>) getSelection();
                    if (!selection.equals(value)) {
                        setValue(selection);
                        callback.update(selection);
                    }
                    return eventConsumed;
                }
            }
        );
    }

    public CheckBoxPanel(int x, int y) {
        this(x, y, VERTICAL_LAYOUT, 5);
    }

    public CheckBoxPanel() {
        this(0, 0);
    }

    /**
     * Callbacks: methods to call when value changes
     */
    public Callback<List<CheckBox>> callback = v -> {};

    public CheckBoxPanel setCallback(Callback<List<CheckBox>> callback) {
        this.callback = callback;
        return this;
    }

    /**
     * Override addChild to add constraints
     */
    @Override
    public Widget<List<CheckBox>> addChild(GraphicalObject child) {
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

    public CheckBoxPanel setDefaultSelection() {
        return this;
    }
}