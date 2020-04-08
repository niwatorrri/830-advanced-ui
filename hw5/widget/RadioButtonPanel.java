package widget;

import java.awt.Color;
import java.util.List;

import behavior.ChoiceBehavior;
import constraint.Constraint;
import graphics.group.LayoutGroup;
import graphics.group.SimpleGroup;
import graphics.object.FilledEllipse;
import graphics.object.GraphicalObject;
import graphics.object.selectable.SelectableEllipse;

public class RadioButtonPanel extends Widget<List<RadioButton>> {

    public RadioButtonPanel(int x, int y, int layout, int offset) {
        if (layout == NO_LAYOUT) {
            this.widget = new SimpleGroup(x, y, 0, 0);
        } else {
            this.widget = new LayoutGroup(x, y, 0, 0, layout, offset);
        }
        this.widget.addBehavior(new ChoiceBehavior(ChoiceBehavior.SINGLE, true));
    }

    public RadioButtonPanel(int x, int y) {
        this(x, y, VERTICAL_LAYOUT, 5);
    }

    public RadioButtonPanel() {
        this(0, 0);
    }

    @Override
    public Widget<List<RadioButton>> addChild(GraphicalObject child) {
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
}