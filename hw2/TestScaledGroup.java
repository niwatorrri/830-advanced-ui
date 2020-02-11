import java.awt.*;

public class TestScaledGroup extends TestFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception {
		new TestScaledGroup(args);
	}

	public TestScaledGroup(String[] args) throws Exception {
		super("TestScaledGroup", 500, 200);

		println("topGroup");
		Group topGroup = new SimpleGroup(0, 0, 500, 200);
		addChild(topGroup);
		
		int nObjects = 5;
		try {
			nObjects = Integer.parseInt(args[0]);
			println("nObjects = " + nObjects);
		} catch (Exception e) {
			println("usage:  TestScaledGroup [# of graphical objects]\n"
					+ "using " + nObjects + " objects by default");
		}

		println("creating black frame");
		topGroup.addChild(new OutlineRect(9, 9, 481, 181, Color.black, 1));

		println("creating ScaledGroup inside black frame");
		ScaledGroup group = new ScaledGroup(10, 10, 480, 180, 1.0, 1.0);
		topGroup.addChild(group);

		println("creating random OutlineRects");
		GraphicalObject[] objects = new GraphicalObject[nObjects];
		Color[] colors = { Color.black, Color.red, Color.blue };
		for (int i = 0; i < nObjects; ++i) {
			objects[i] = new OutlineRect(random(180), random(180), 30 + random(20),
					30 + random(20), (Color) random(colors), 3);
			group.addChild(objects[i]);
        }
		Graphics2D g = (Graphics2D) buffer.getGraphics();
		group.addChild(new Text(g, "going", 100, 100, new Font("Monospaced", Font.PLAIN,
                10), Color.black));
        redraw(topGroup);
		pause();

        println("rescaling ScaledGroup: shrinking x");
        group.setScaleX(0.5);
        redraw(topGroup);
		pause();

        println("rescaling ScaledGroup: shrinking y");
        group.setScaleY(0.5);
        redraw(topGroup);
		pause();

        println("rescaling ScaledGroup: back to normal scale");
        group.setScaleX(1.0);
        group.setScaleY(1.0);
        redraw(topGroup);
		pause();

        println("rescaling ScaledGroup: enlarge x");
        group.setScaleX(2.0);
        redraw(topGroup);
        pause();

        println("nesting ScaledGroup: should be back to normal");
        ScaledGroup highGroup = new ScaledGroup(10, 10, 1000, 200, 0.5, 1.0);
        topGroup.addChild(highGroup);
		topGroup.removeChild(group);
		highGroup.addChild(group);
		group.moveTo(0, 0);
		redraw(topGroup);
        pause();
		println("close the window to exit");
	}
}