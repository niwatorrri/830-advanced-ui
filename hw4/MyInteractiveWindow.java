import java.awt.*;

import graphics.group.Group;
import graphics.group.SimpleGroup;
import graphics.object.GraphicalObject;
import graphics.object.selectable.SelectableOutlineRect;

import constraint.Constraint;
import constraint.SetupConstraint;

import behavior.MoveBehavior;
import behavior.ChoiceBehavior;
import behavior.NewRectBehavior;
import behavior.InteractiveWindowGroup;

public class MyInteractiveWindow extends InteractiveWindowGroup {
    /**
     * An example interactive window that allows users to make new rectangles,
     * move them by click and drag, and choose them with a single choice behavior
     */
    private static final long serialVersionUID = 1L;

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 400;

    public static void main(String[] args) {
        new MyInteractiveWindow();
    }

    public MyInteractiveWindow() {
        super("MyInteractiveWindow", WINDOW_WIDTH, WINDOW_HEIGHT);
        Group topGroup = new SimpleGroup(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        addChild(topGroup);
        writeMyInteractions(topGroup);
        redraw(topGroup);
    }

    public void writeMyInteractions(Group topGroup) {
        Group g = new SimpleGroup(0, 0, 400, 400);
        topGroup.addChild(g);

        SetupConstraint<SelectableOutlineRect> rectColorConstraint = o -> {
            o.setColor(new Constraint<Color>(o.useSelected(), o.useInterimSelected()) {
                public Color getValue() {
                    if (o.isSelected()) {
                        return o.isInterimSelected() ? Color.BLUE : Color.GREEN;
                    } else {
                        return o.isInterimSelected() ? Color.YELLOW : Color.BLACK;
                    }
                }
            });
        };
        for (GraphicalObject o : g.getChildren()) {
            rectColorConstraint.setup((SelectableOutlineRect) o);
        }

        addBehaviors(
            new MoveBehavior().setGroup(g).setPriority(0),
            new ChoiceBehavior(ChoiceBehavior.SINGLE, true).setGroup(g).setPriority(0),
            new NewRectBehavior(
                NewRectBehavior.OUTLINE_RECT, Color.BLACK, 2, rectColorConstraint).setGroup(g)    
        );
    }
}