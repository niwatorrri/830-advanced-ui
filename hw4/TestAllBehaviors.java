import java.awt.*;
import java.io.IOException;


public class TestAllBehaviors extends InteractiveWindowGroup {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new TestAllBehaviors();
	}

	/* Note that this is just to give you an idea of how things should work. You will need
	 * to edit this file to match up with your actual design
	 */
	public TestAllBehaviors() {
		super("TestAllBehaviors", 400, 600);
		Group topGroup = new SimpleGroup (0,0,400, 600);
		addChild(topGroup);
		
		Group objsGroup = new SimpleGroup (0,0,400, 600);
		topGroup.addChild (objsGroup);
		
		SelectableOutlineRect r1 = new SelectableOutlineRect(20, 20, 50, 80, Color.black, 1);
		SelectableFillRect r2 = new SelectableFillRect (80, 20, 50, 50, Color.black);
		SelectableLine l1 = new SelectableLine (30, 80, 50, 200, Color.black, 4);
		Graphics2D g = (Graphics2D) buffer.getGraphics();
		SelectableText t1 = new SelectableText(g, "going", 10, 350, new Font("Monospaced",
						Font.PLAIN, 10), Color.black);
		objsGroup.addChild(r1);
		objsGroup.addChild(r2);
		objsGroup.addChild(l1);
		objsGroup.addChild(t1);
	
		* Add constraint(s) to r1, r2, and t1 so that the color of each is:
		*	if (this.selected) {
		*		if (this.interimSelected) blue; //both
		*		else green; // just selected and not interimSelected
		*	}
		*	else if (interimSelected) yellow; // just interimSelected and not selected
		*	else black;
		
		println ("left to select; shift-left to create a rect; control-left of create a line; control-shift to move");
		
		* Create a ChoiceBehavior in MULTIPLE mode that operates on the members of objsGroup with event LEFT_MOUSEDOWN
		* Create a NewBehavior that creates SelectableFillRect in objsGroup with startEvent SHIFT_LEFT_MOUSEDOWN
		* Create a NewBehavior that creates SelectableLines in objsGroup with startEvent CONTROL_LEFT_MOUSEDOWN
		* Create a MoveBehavior that moves an object in objsGroup with startEvent CONTROL_SHIFT_LEFT_MOUSEDOWN

		* start the main event loop so the behaviors start running
		
		println("close the window to stop");
	}

}
