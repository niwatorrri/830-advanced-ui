import java.awt.*;
import java.util.List;

public class TestSimpleGroup extends TestFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new TestSimpleGroup(args);
	}

	public TestSimpleGroup(String[] args) {
		super("TestSimpleGroup", 200, 200);

		int nObjects = 4;
		try {
			nObjects = Integer.parseInt(args[0]);
			println("nObjects = " + nObjects);
		} catch (Exception e) {
			println("usage:  TestSimpleGroup [# of graphical objects]\n"
					+ "using " + nObjects + " objects by default");
		}

		println("creating topGroup");
		Group topGroup = new SimpleGroup(0, 0, 200, 200);
		addChild(topGroup);

		println("creating black frame");
		topGroup.addChild(new OutlineRect(9, 9, 182, 182, Color.black, 1));

		println("creating SimpleGroup inside black frame");
		Group group = new SimpleGroup(10, 10, 180, 180);
		topGroup.addChild(group);
		
		println("creating 3 Rects in SimpleGroup");
		FilledRect r1 = new FilledRect(1, 1, 40, 20, Color.red);
		FilledRect r2 = new FilledRect(1, 10, 20, 40, Color.green);
		FilledRect r3 = new FilledRect(10, 30, 50, 50, Color.yellow);
		group.addChild(r1);
		group.addChild(r2);
		group.addChild(r3);
		redraw(topGroup);
		pause();

		println("remove one child in SimpleGroup");
		group.removeChild(r1);
		redraw(topGroup);
		pause();

		println("bring a child to front in SimpleGroup");
		group.bringChildToFront(r2);
		redraw(topGroup);
		pause();

		println("move one child from a copy of children");
		List<GraphicalObject> children = group.getChildren();
		children.get(0).moveTo(50, 50);
		redraw(topGroup);
		pause();
		
		println("moving group - rects should move too");
		group.moveTo(20, 30);
		redraw(topGroup);
		pause();

		println("moving group back to where it was");
		group.moveTo(10, 10);
		redraw(topGroup);
		pause();
		
		println("creating subgroup at (100, 20) with rectangles in it");
		Group subgroup = new SimpleGroup(100, 20, 50, 50);
		group.addChild(subgroup);
		subgroup.addChild(new FilledRect(0, 0, 10, 20, Color.blue));
		subgroup.addChild(new FilledRect(5, 10, 20, 20, Color.cyan));
		subgroup.addChild(new FilledRect(15, 20, 50, 50, Color.orange));
		redraw(topGroup);
		pause();

		println("creating Rects at random places");
		GraphicalObject[] objects = new GraphicalObject[nObjects];
		Color[] colors = { Color.black, Color.red, Color.blue };
		for (int i = 0; i < nObjects; ++i) {
			objects[i] = new OutlineRect(-20 + random(200), -20 + random(200),
					random(100), random(100), (Color) random(colors), 1);
			try {
				group.addChild(objects[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		redraw(topGroup);
		pause();

		println("moving rectangles 1000 times");
		println("close the window to stop");
		for (int i = 0; i < 1000; ++i) {
			GraphicalObject gobj = (GraphicalObject) random(objects);
			gobj.moveTo(-20 + random(200), -20 + random(200));
			group.bringChildToFront(gobj);
			redraw(topGroup);
			sleep(500);
		}
	}
}