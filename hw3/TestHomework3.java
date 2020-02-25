import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

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
            Group windowgroup = new SimpleGroup(0, 0, 300, 400);
            Group group = new SimpleGroup(0, 0, 300, 400);
            addChild(windowgroup);

            windowgroup.addChild(group);
            group.addChild(blueRect);
            group.addChild(redRect);
            redraw(windowgroup);

            println("2. moving blue to 30,30, red shouldn't move");
            pause();
            blueRect.moveTo(30, 90);
            redraw(windowgroup);

            println("3. adding constraint to red rect to be at right of blue");
            println("     red should move to be at 80,30");
            pause();

            // redRect.setX(new Constraint<Integer>(
            //     redRect.getX(),
            //     blueRect        // dependencies
            // ) {
            //     public Integer getValue() {
            //         return ((OutlineRect) dependencies.get(0)).getX() + 50;
            //     }
            // });

            redRect.setX(new Constraint<Integer>() {
                private OutlineRect dependentBlueRect = blueRect;
                public Integer getValue() {
                    return dependentBlueRect.getX() + 50;
                }
            });
            /*
             * something like the following: Constraint<Integer> xc = new
             * Constraint<Integer> ( **New constraint = blueRect's right side ** );
             * redRect.setX ( xc );
             * Constraint<Integer> yc = new Constraint<Integer> ( **New
             * constraint = blueRect's Y ** );
             * redRect.setY ( yc );
             */
            redraw(windowgroup);

            println("4. Move Blue, red should move automatically");
            pause();
            blueRect.moveTo(0, 0);
            redraw(windowgroup);

            // *** Add in some more constraints and tests here to show the range of
            // *** what you can express and the appropriate syntax

            println("DONE. close the window to stop");

        } catch (Exception e) {
            println("got an exception " + e);
        }
    }
}