package ui.toolkit.widget;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import ui.toolkit.behavior.BehaviorEvent;
import ui.toolkit.behavior.ChoiceBehavior;
import ui.toolkit.constraint.Constraint;
import ui.toolkit.graphics.group.LayoutGroup;
import ui.toolkit.graphics.group.SimpleGroup;
import ui.toolkit.graphics.object.GraphicalObject;
import ui.toolkit.graphics.object.selectable.SelectableFilledRect;

public class ButtonPanel extends Widget<List<Button>> {
    private ChoiceBehavior choiceBehavior;

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
            choiceBehavior = new ChoiceBehavior(selectionType, false) {
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

    @Override
    public ButtonPanel addChildren(GraphicalObject... children) {
        for (GraphicalObject child : children) {
            addChild(child);
        }
        return this;
    }

    public ButtonPanel setSelection(String type) {
        choiceBehavior.select(type);
        return this;
    }
}