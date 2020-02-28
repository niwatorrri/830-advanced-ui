import java.awt.*;

public class TestHomework3 extends TestFrame {

    public static void main(String[] args) {
        new TestHomework3(args);
    }

    public TestHomework3(String[] args) {
        super("TestHomework3", 400, 400);

        try {
            println("1. Creating blue, red and black rects");
            OutlineRect blueRect = new OutlineRect(0, 0, 50, 80, Color.blue, 5);
            OutlineRect redRect = new OutlineRect(100, 0, 50, 80, Color.red, 1);
            OutlineRect blackRect = new OutlineRect(250, 0, 50, 80, Color.black, 3);

            Group windowGroup = new SimpleGroup(0, 0, 400, 400);
            Group group = new SimpleGroup(0, 0, 400, 400);
            addChild(windowGroup);
            windowGroup.addChild(group);
            group.addChild(blueRect);
            group.addChild(redRect);
            group.addChild(blackRect);
            redraw(windowGroup);

            println("2. Moving blue to (100, 0), others shouldn't move");
            pause();
            blueRect.moveTo(80, 0);
            redraw(windowGroup);

            println("3. Adding constraint on x of red rect to be next to blue");
            pause();
            redRect.setX(new Constraint<Integer>(blueRect.useX()) {
                public Integer getValue() {
                    return blueRect.getX() + 50;
                }
            });
            redraw(windowGroup);

            println("4. Setting a new constraint on x of red rect to be next to black");
            println("   (Test: replace the original constraint with a new one)");
            pause();
            redRect.setX(new Constraint<Integer>(blackRect.useX()) {
                public Integer getValue() {
                    return blackRect.getX() + 50;
                }
            });
            redraw(windowGroup);

            println("5. Constraint on x of red rect to be next to the leftmost rect");
            println("   (Test: Constraint on two objects)");
            pause();
            redRect.setX(new Constraint<Integer>(
                blueRect.useX(), blackRect.useX()
            ) {
                public Integer getValue() {
                    return Math.min(blueRect.getX(), blackRect.getX()) + 50;
                }
            });
            redraw(windowGroup);

            println("6. Switching positions of blue and black rects");
            println("   Red rect should not move");
            pause();
            int blackX = blackRect.getX(), blueX = blueRect.getX();
            blueRect.setX(blackX);
            blackRect.setX(blueX);
            redraw(windowGroup);

            // println("4. Move Blue, red should move automatically");
            // pause();
            // blueRect.moveTo(0, 0);
            // redraw(windowGroup);

            println("   Two constraints on the same object");

            println("   Set new constraint on the object");
            println("   Constraint on more than one attribute");
            println("   Constraint on variables of more than one type");
            println("   Stress test");
            println("   Test loop");
            // *** Add in some more constraints and tests here to show the range of
            // *** what you can express and the appropriate syntax

            println("DONE! Close the window to stop");

        } catch (Exception e) {
            println("Got an exception " + e);
        }
    }
}