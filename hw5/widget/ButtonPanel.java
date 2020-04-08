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
import graphics.object.selectable.SelectableFilledRect;

public class ButtonPanel extends Widget<List<Button>> {
    private boolean finalFeedback;

    public static final int SINGLE = ChoiceBehavior.SINGLE;
    public static final int MULTIPLE = ChoiceBehavior.MULTIPLE;

    /**
     * ButtonPanel constructor
     * 
     * @param x
     * @param y
     * @param finalFeedback
     * @param selectionType
     * @param layout
     * @param offset
     */
    public ButtonPanel(int x, int y, boolean finalFeedback, int selectionType, int layout, int offset) {
        if (layout == NO_LAYOUT) {
            this.widget = new SimpleGroup(x, y, 0, 0);
        } else {
            this.widget = new LayoutGroup(x, y, 0, 0, layout, offset);
        }

        this.value = new ArrayList<Button>();
        this.widget.addBehavior(
            new ChoiceBehavior(selectionType, false) {
                @Override
                @SuppressWarnings("unchecked")
                public boolean stop(BehaviorEvent event) {
                    boolean eventConsumed = super.stop(event);
                    List<Button> selection = (List<Button>)(List<?>) getSelection();
                    if (!selection.equals(value)) {
                        setValue(selection);
                        callback.update(selection);
                    }
                    return eventConsumed;
                }
            }
        );
        this.finalFeedback = finalFeedback;
    }

    public ButtonPanel(int x, int y, boolean finalFeedback, int selectionType) {
        this(x, y, finalFeedback, selectionType, VERTICAL_LAYOUT, 10);
    }

    public ButtonPanel() {
        this(0, 0, true, SINGLE);
    }

    /**
     * Override addChild to add constraints
     */
    @Override
    public Widget<List<Button>> addChild(GraphicalObject child) {
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

    public ButtonPanel setDefaultSelection() {
        return this;
    }
}