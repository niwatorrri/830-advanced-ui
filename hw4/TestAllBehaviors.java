import java.awt.*;

import graphics.group.*;
import graphics.object.*;
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

	/*
	 * Note that this is just to give you an idea of how things should work. You
	 * will need to edit this file to match up with your actual design
	 */
	public TestAllBehaviors() {
		super("TestAllBehaviors", WINDOW_WIDTH, WINDOW_HEIGHT);
		Group topGroup = new SimpleGroup(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		addChild(topGroup);

		testIntegration(topGroup);

		redraw(topGroup);
	}

	public void testIntegration(Group topGroup) {
		// Group objectGroup = new SimpleGroup(0, 0, 400, 400);
		Group g = new SimpleGroup(50, 50, 300, 300);
		topGroup.addChild(g);

		SelectableOutlineRect r1 = new SelectableOutlineRect(20, 20, 50, 80, Color.black, 1);
		SelectableFilledRect r2 = new SelectableFilledRect(80, 20, 50, 50, Color.black);
		SelectableLine l1 = new SelectableLine(30, 80, 50, 200, Color.black, 4);
		SelectableText t1 = new SelectableText(null, "going", 10, 350, Text.DEFAULT_FONT, Color.BLACK);
		/*
		 * Add constraint(s) to r1, r2, and t1 so that the color of each is: if
		 * (this.selected) { if (this.interimSelected) blue; //both else green; // just
		 * selected and not interimSelected } else if (interimSelected) yellow; // just
		 * interimSelected and not selected else black;
		 */
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


		// println("left to select; shift-left to create a rect; control-left of create a line; control-shift to move");

		/*
		 * Create a ChoiceBehavior in MULTIPLE mode that operates on the members of
		 * objectGroup with event LEFT_MOUSEDOWN
		 * Create a NewBehavior that creates SelectableFillRect in objectGroup with
		 * startEvent SHIFT_LEFT_MOUSEDOWN
		 * Create a NewBehavior that creates SelectableLines in objectGroup with startEvent
		 * CONTROL_LEFT_MOUSEDOWN
		 * Create a MoveBehavior that moves an object in objectGroup with startEvent
		 * CONTROL_SHIFT_LEFT_MOUSEDOWN
		 */
		addBehavior(new ChoiceBehavior(ChoiceBehavior.MULTIPLE, false).setGroup(g));
		addBehavior(
			new NewRectBehavior(NewRectBehavior.OUTLINE_RECT, Color.BLACK, 2, rectColorConstraint)
				.setGroup(g)
				.setStartEvent(new BehaviorEvent(SHIFT_MODIFIER, LEFT_MOUSE_KEY, MOUSE_DOWN_ID))
		);
		addBehavior(
			new NewLineBehavior(Color.BLACK, 2)
				.setGroup(g)
				.setStartEvent(new BehaviorEvent(CONTROL_MODIFIER, LEFT_MOUSE_KEY, MOUSE_DOWN_ID))
		);
		addBehavior(
			new MoveBehavior()
				.setGroup(g)
				.setStartEvent(new BehaviorEvent(
					CONTROL_MODIFIER | SHIFT_MODIFIER, LEFT_MOUSE_KEY, MOUSE_DOWN_ID))
		);

		// test: group within group
		// test: selectable group

		// println("close the window to stop");
	}
}