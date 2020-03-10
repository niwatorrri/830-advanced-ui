import java.awt.*;

import graphics.object.*;
import graphics.group.*;

public class TestOutlineRect extends TestFrame {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new TestOutlineRect(args);
    }

    public TestOutlineRect(String[] args) {
        super("TestOutlineRect", 200, 200);
        int lineThickness = 3;
        try {
            lineThickness = Integer.parseInt(args[0]);
            println("line thickness = " + lineThickness);
        } catch (Exception e) {
            println("usage: TestOutlineRect [line thickness]\n"
            + "using line thickness = "
            + lineThickness + " by default");
        }
        println("creating topGroup");
        Group topGroup = new SimpleGroup(0, 0, 200, 200);
        addChild(topGroup);

        println("creating OutlineRect");
        OutlineRect r = new OutlineRect(10, 10, 50, 50, Color.red, lineThickness);
        topGroup.addChild(r);
        redraw(topGroup);
        pause();

        println("moving rectangle with setX(), setY()");
        for (int x = 10; x < 150; x += 30) {
            r.setX(x);
            for (int y = 10; y < 150; y += 30) {
                r.setY(y);
                redraw(topGroup);
                sleep(100);
            }
        }
        println("final bounding box is " + r.getBoundingBox());
        println("final x/y position is " + r.getX() + "," + r.getY());
        redraw(topGroup); /* this is missing from the one online */
        pause();

        println("changing to blue");
        r.setColor(Color.blue);
        redraw(topGroup);
        pause();

        println("moving rectangle with moveTo()");
        for (int x = 10; x < 150; x += 30) {
            for (int y = 10; y < 150; y += 30) {
                r.moveTo(x, y);
                redraw(topGroup);
                sleep(100);
            }
        }
        println("final bounding box is " + r.getBoundingBox());
        println("final x/y position is " + r.getX() + "," + r.getY());
        redraw(topGroup); /* this is missing from the one online */
        pause();

        println("doubling line thickness to " + lineThickness * 2);
        r.setLineThickness(lineThickness * 2);
        redraw(topGroup);
        pause();

        println("moving rectangle with moveTo()");
        for (int x = 10; x < 150; x += 30) {
            for (int y = 10; y < 150; y += 30) {
                r.moveTo(x, y);
                redraw(topGroup);
                sleep(100);
            }
        }
        println("final bounding box is " + r.getBoundingBox());
        println("final x/y position is " + r.getX() + "," + r.getY());
        println("close the window to exit");
    }
}
