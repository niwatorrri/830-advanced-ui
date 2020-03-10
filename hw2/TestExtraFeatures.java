import java.awt.*;

import graphics.object.*;
import graphics.group.*;

public class TestExtraFeatures extends TestFrame {
    /**
     * Test for extra features
     * 1. Ellipses
     * 2. Grid layout
     */

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws Exception {
        new TestExtraFeatures(args);
    }

    public TestExtraFeatures(String[] args) throws Exception {
        super("TestExtraFeatures", 500, 200);

        println("topGroup");
        Group topGroup = new SimpleGroup(0, 0, 500, 200);
        addChild(topGroup);
        
        int nObjects = 7;
        try {
            nObjects = Integer.parseInt(args[0]);
            println("nObjects = " + nObjects);
        } catch (Exception e) {
            println("usage:  TestExtraFeatures [# of graphical objects]\n"
                    + "using " + nObjects + " objects by default");
        }

        println("creating black frame");
        topGroup.addChild(new OutlineRect(9, 9, 481, 181, Color.black, 1));

        println("creating groups inside black frame");
        Group simpleGroup = new SimpleGroup(10, 10, 480, 180);
        LayoutGroup layoutGroup = new LayoutGroup(10, 10, 480, 180, Group.GRID, 3, 3);
        topGroup.addChild(simpleGroup);

        println("TESTING ELLIPSES");
        println("creating random Ellipses");
        Ellipse[] objects = new Ellipse[nObjects];
        Color[] colors = { Color.black, Color.red, Color.blue };
        for (int i = 0; i < nObjects; ++i) {
            objects[i] = new Ellipse(random(200), random(200), 30 + random(20),
                    30 + random(20), (Color) random(colors), 1 + random(5));
            simpleGroup.addChild(objects[i]);
        }
        redraw(topGroup);
        pause();
        
        println("moving ellipses 10 times");
        for (int i = 0; i < 10; ++i) {
            Ellipse gobj = (Ellipse) random(objects);
            gobj.moveTo(-20 + random(200), -20 + random(200));
            redraw(topGroup);
            sleep(500);
        }
        pause();
        
        println("doubling thickness of all red ellipses");
        for (Ellipse e: objects) {
            if (e.getColor() == Color.red) {
                e.setLineThickness(e.getLineThickness() * 2);
            }
        }
        redraw(topGroup);
        pause();

        println("TESTING GRID LAYOUT");
        println("putting children in a 3x3 grid layout group");
        for (Ellipse obj: objects) {
            simpleGroup.removeChild(obj);
            layoutGroup.addChild(obj);
        }
        topGroup.addChild(layoutGroup);
        redraw(topGroup);
        pause();

        println("shuffling objects 5 times");
        Ellipse front = objects[objects.length - 1];
        for (int i = 0; i < 5; ++i) {
            Ellipse gobj;
            while ((gobj = (Ellipse) random(objects)) == front)
                ;
            layoutGroup.bringChildToFront(gobj);
            front = gobj;
            redraw(topGroup);
            sleep(1000);
        }
        pause();
        
        println("changing layout to 2x3");
        layoutGroup.setNRows(2);
        redraw(topGroup);
        pause();

        println("changing layout to 2x4");
        layoutGroup.setNColumns(4);
        redraw(topGroup);
        pause();

        println("resize to children");
        layoutGroup.resizeToChildren();
        redraw(topGroup);
        println("close the window to exit");
    }
}