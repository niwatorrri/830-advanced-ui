import java.awt.*;

import graphics.group.*;
import graphics.object.*;
import graphics.group.selectable.*;
import graphics.object.selectable.*;
import constraint.*;
import behavior.*;

import static behavior.BehaviorEvent.*;

public class TestAllBehaviors extends InteractiveWindowGroup {
    private static final long serialVersionUID = 1L;

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;

    public static void main(String[] args) {
        new TestAllBehaviors();
    }

    public TestAllBehaviors() {
        super("TestAllBehaviors", WINDOW_WIDTH, WINDOW_HEIGHT);
        Group topGroup = new SimpleGroup(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        addChild(topGroup);

        Group layoutGroup = new LayoutGroup(
            0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, LayoutGroup.GRID, 0, 2, 4);
        topGroup.addChildren(layoutGroup,
            new Line(0, 150, WINDOW_WIDTH, 150, Color.BLACK, 1),
            new Line(0, 300, WINDOW_WIDTH, 300, Color.BLACK, 1),
            new Line(150, 0, 150, 300, Color.BLACK, 1),
            new Line(300, 0, 300, 300, Color.BLACK, 1),
            new Line(450, 0, 450, 300, Color.BLACK, 1)
        );

        testChoiceBehavior(layoutGroup);
        testMoveGridBehavior(layoutGroup);
        testSelectableGroup(layoutGroup);
        testNewTextBehavior(layoutGroup);
        layoutGroup.resizeToChildren();

        testIntegration(topGroup);
        redraw(topGroup);
    }

    public void testChoiceBehavior(Group topGroup) {
        /**
         * Test four configurations of ChoiceBehavior
         */
        Group g1 = new SimpleGroup(0, 0, 150, 150).addChildren(
            new SelectableOutlineRect(30, 30, 20, 20, Color.BLACK, 2),
            new SelectableOutlineRect(75, 60, 20, 20, Color.BLACK, 2),
            new Text("Choice:\nSingle + firstOnly", 10, 110)
        );
        Group g2 = new SimpleGroup(150, 0, 150, 150).addChildren(
            new SelectableOutlineRect(30, 30, 20, 20, Color.BLACK, 2),
            new SelectableOutlineRect(75, 60, 20, 20, Color.BLACK, 2),
            new Text("Choice:\nSingle + !firstOnly", 10, 110)
        );
        Group g3 = new SimpleGroup(300, 0, 150, 150).addChildren(
            new SelectableOutlineRect(30, 30, 20, 20, Color.BLACK, 2),
            new SelectableOutlineRect(75, 60, 20, 20, Color.BLACK, 2),
            new Text("Choice:\nMultiple + firstOnly", 10, 110)
        );
        Group g4 = new SimpleGroup(450, 0, 150, 150).addChildren(
            new SelectableOutlineRect(30, 30, 20, 20, Color.BLACK, 2),
            new SelectableOutlineRect(75, 60, 20, 20, Color.BLACK, 2),
            new Text("Choice:\nMultiple + !firstOnly", 10, 110)
        );

        setupColorConstraintForGroup(g1);
        setupColorConstraintForGroup(g2);
        setupColorConstraintForGroup(g3);
        setupColorConstraintForGroup(g4);
        topGroup.addChildren(g1, g2, g3, g4);

        addBehaviors(
            new ChoiceBehavior(ChoiceBehavior.SINGLE, true).setGroup(g1),
            new ChoiceBehavior(ChoiceBehavior.SINGLE, false).setGroup(g2),
            new ChoiceBehavior(ChoiceBehavior.MULTIPLE, true).setGroup(g3),
            new ChoiceBehavior(ChoiceBehavior.MULTIPLE, false).setGroup(g4)
        );
    }

    private void setupColorConstraintForGroup(Group group) {
        SetupConstraint rectColorConstraint = dependencies -> {
            SelectableOutlineRect o = (SelectableOutlineRect) dependencies[0];
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
        for (GraphicalObject o : group.getChildren()) {
            if (o instanceof SelectableOutlineRect) {
                rectColorConstraint.setup(o);
            }
        }
    }

    public void testMoveGridBehavior(Group topGroup) {
        /**
         * Test MoveBehavior gridSize parameter
         */
        Group g1 = new SimpleGroup(0, 0, 150, 150).addChildren(
            new OutlineRect(30, 30, 20, 20, Color.BLACK, 2),
            new OutlineRect(75, 60, 20, 20, Color.BLACK, 2),
            new Text("Move: grid size = 15", 10, 110)
        );
        Group g2 = new SimpleGroup(0, 0, 150, 150).addChildren(
            new OutlineRect(30, 30, 20, 20, Color.BLACK, 2),
            new OutlineRect(75, 60, 20, 20, Color.BLACK, 2),
            new Text("Move: press S to start\npress A to stop", 10, 110)
        );
        topGroup.addChildren(g1, g2);

        addBehaviors(
            new MoveBehavior(15).setGroup(g1),
            new MoveBehavior()
                .setGroup(g2)
                .setStartEvent(new BehaviorEvent(0, 'S', KEY_DOWN_ID))
                .setStopEvent(new BehaviorEvent(0, 'A', KEY_DOWN_ID))
        );
    }

    public void testSelectableGroup(Group topGroup) {
        /**
         * Test SelectableGroup example
         */
        Group g1 = new SelectableSimpleGroup(0, 0, 75, 150).addChildren(
            new SelectableOutlineRect(30, 30, 20, 20, Color.BLACK, 2),
            new SelectableOutlineRect(30, 60, 20, 20, Color.BLACK, 2)
        );
        Group g2 = new SelectableSimpleGroup(75, 0, 75, 150).addChildren(
            new SelectableOutlineRect(0, 30, 20, 20, Color.BLACK, 2),
            new SelectableOutlineRect(0, 60, 20, 20, Color.BLACK, 2)
        );
        Group g = new SimpleGroup(0, 0, 150, 150).addChildren(g1, g2,
            new Text("SelectableGroup: choice\non left/right group", 10, 110)
        );
        topGroup.addChild(g);

        SetupConstraint groupColorConstraint = dependencies -> {
            SelectableSimpleGroup sg = (SelectableSimpleGroup) dependencies[0];
            for (GraphicalObject child : sg.getChildren()) {
                if (child instanceof SelectableOutlineRect) {
                    SelectableOutlineRect o = (SelectableOutlineRect) child;
                    o.setColor(new Constraint<Color>(sg.useSelected(), sg.useInterimSelected()) {
                        public Color getValue() {
                            if (sg.isSelected()) {
                                return sg.isInterimSelected() ? Color.BLUE : Color.GREEN;
                            } else {
                                return sg.isInterimSelected() ? Color.YELLOW : Color.BLACK;
                            }
                        }
                    });
                }
            }
        };
        groupColorConstraint.setup(g1);
        groupColorConstraint.setup(g2);
        addBehavior(new ChoiceBehavior(ChoiceBehavior.SINGLE, false).setGroup(g));
    }

    public void testNewTextBehavior(Group topGroup) {
        /**
         * Test NewTextBehavior
         */
        Group g = new SimpleGroup(0, 0, 150, 150).addChild(
            new Text("NewText: click to\nmake text", 10, 110)
        );
        topGroup.addChild(g);
        addBehavior(new NewTextBehavior("text", Text.DEFAULT_FONT, Color.BLACK).setGroup(g));
    }

    private Color colorConstraint(SelectableGraphicalObject o) {
        if (o.isSelected()) {
            return o.isInterimSelected() ? Color.BLUE : Color.GREEN;
        } else {
            return o.isInterimSelected() ? Color.YELLOW : Color.BLACK;
        }
    }

    public void testIntegration(Group topGroup) {
        /**
         * An integrated test
         */
        Group g = new SimpleGroup(0, 300, 600, 300);
        topGroup.addChildren(g,
            new SimpleGroup(0, 300, 600, 300)
                .addChild(new Text(
                    "Do multiple choice with LEFT_MOUSE_DOWN\n" +
                    "Create selectable filled rects with SHIFT_LEFT_MOUSE_DOWN\n" +
                    "Create selectable lines with CONTROL_LEFT_MOUSE_DOWN\n" +
                    "Move objects with CONTROL_SHIFT_LEFT_MOUSE_DOWN", 120, 220)
                )
        );

        SelectableOutlineRect r1 = new SelectableOutlineRect(120, 20, 50, 80, Color.black, 1);
        SelectableFilledRect r2 = new SelectableFilledRect(280, 20, 50, 50, Color.black);
        SelectableLine l = new SelectableLine(200, 80, 250, 100, Color.black, 4);
        SelectableText t = new SelectableText(null, "going", 300, 150, Text.DEFAULT_FONT, Color.BLACK);
        g.addChildren(r1, r2, l, t);

        SetupConstraint rectColorConstraint = dependencies -> {
            SelectableFilledRect o = (SelectableFilledRect) dependencies[0];
            o.setColor(new Constraint<Color>(o.useSelected(), o.useInterimSelected()) {
                public Color getValue() {
                    return colorConstraint(o);
                }
            });
        };
        rectColorConstraint.setup(r2);

        SetupConstraint lineColorConstraint = dependencies -> {
            SelectableLine o = (SelectableLine) dependencies[0];
            o.setColor(new Constraint<Color>(o.useSelected(), o.useInterimSelected()) {
                public Color getValue() {
                    return colorConstraint(o);
                }
            });
        };
        lineColorConstraint.setup(l);

        r1.setColor(new Constraint<Color>(r1.useSelected(), r1.useInterimSelected()) {
            public Color getValue() {
                return colorConstraint(r1);
            }
        });

        t.setColor(new Constraint<Color>(t.useSelected(), t.useInterimSelected()) {
            public Color getValue() {
                return colorConstraint(t);
            }
        });

        /*
         * Create a ChoiceBehavior in MULTIPLE mode that operates with event LEFT_MOUSE_DOWN
         * Create a NewBehavior that creates SelectableFilledRect with startEvent SHIFT_LEFT_MOUSE_DOWN
         * Create a NewBehavior that creates SelectableLine with startEvent CONTROL_LEFT_MOUSE_DOWN
         * Create a MoveBehavior that moves an object with startEvent CONTROL_SHIFT_LEFT_MOUSE_DOWN
         */
        addBehavior(new ChoiceBehavior(ChoiceBehavior.MULTIPLE, false).setGroup(g));
        addBehavior(
            new NewRectBehavior(NewRectBehavior.FILLED_RECT, Color.BLACK, 2, rectColorConstraint)
                .setGroup(g)
                .setStartEvent(new BehaviorEvent(SHIFT_MODIFIER, LEFT_MOUSE_KEY, MOUSE_DOWN_ID))
        );
        addBehavior(
            new NewLineBehavior(Color.BLACK, 2, lineColorConstraint)
                .setGroup(g)
                .setStartEvent(new BehaviorEvent(CONTROL_MODIFIER, LEFT_MOUSE_KEY, MOUSE_DOWN_ID))
        );
        addBehavior(
            new MoveBehavior()
                .setGroup(g)
                .setStartEvent(new BehaviorEvent(
                    CONTROL_MODIFIER | SHIFT_MODIFIER, LEFT_MOUSE_KEY, MOUSE_DOWN_ID))
        );
    }
}