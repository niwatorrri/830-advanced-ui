import java.awt.*;

public class TestHomework3 extends TestFrame {

    public static void main(String[] args) {
        new TestHomework3(args);
    }

    public TestHomework3(String[] args) {
        super("TestHomework3", 400, 400);

        try {
            println("1. creating blue and red rects");
            OutlineRect blueRect = new OutlineRect(0, 0, 50, 80, Color.blue, 5);
            OutlineRect redRect = new OutlineRect(100, 0, 50, 80, Color.red, 1);
            OutlineRect blackRect = new OutlineRect(250, 0, 50, 80, Color.black, 1);
            Group windowGroup = new SimpleGroup(0, 0, 300, 400);
            Group group = new SimpleGroup(0, 0, 300, 400);
            addChild(windowGroup);

            windowGroup.addChild(group);
            group.addChild(blueRect);
            group.addChild(redRect);
            group.addChild(blackRect);
            redraw(windowGroup);

            println("2. moving blue to 30,30, red shouldn't move");
            pause();
            // blueRect.moveTo(30, 90);
            blueRect.moveTo(100, 0);
            redraw(windowGroup);

            println("3. adding constraint to red rect to be at right of blue");
            println("     red should move to be at 80,30");
            redRect.setX(new Constraint<Integer>(blueRect.useX()) {
                public Integer getValue() {
                    return blueRect.getX() + 50;
                }
            });
            redraw(windowGroup);
            pause();

            blackRect.setX(new Constraint<Integer>(
                blueRect.useX(), redRect.useX()
            ) {
                public Integer getValue() {
                    return (blueRect.getX() + redRect.getX()) / 2;
                }
            });
            redraw(windowGroup);

            println("4. Move Blue, red should move automatically");
            pause();
            blueRect.moveTo(0, 0);
            redraw(windowGroup);

            // *** Add in some more constraints and tests here to show the range of
            // *** what you can express and the appropriate syntax

            println("DONE. close the window to stop");

        } catch (Exception e) {
            println("got an exception " + e);
        }
    }
}