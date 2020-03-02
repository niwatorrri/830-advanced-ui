import java.awt.*;

public class TestHomework3 extends TestFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    public static void main(String[] args) {
        new TestHomework3(args);
    }

    public TestHomework3(String[] args) {
        super("TestHomework3", WIDTH, HEIGHT);

        try {
            println("============= Basic Tests =============");
            println("1. Creating blue, red and black rects");
            OutlineRect blueRect = new OutlineRect(0, 0, 50, 80, Color.blue, 5);
            OutlineRect redRect = new OutlineRect(100, 0, 50, 80, Color.red, 1);
            OutlineRect blackRect = new OutlineRect(250, 0, 50, 80, Color.black, 3);

            Group windowGroup = new SimpleGroup(0, 0, WIDTH, HEIGHT);
            Group group = new SimpleGroup(0, 0, WIDTH, HEIGHT);
            addChild(windowGroup);
            windowGroup.addChild(group);
            group.addChild(blueRect);
            group.addChild(redRect);
            group.addChild(blackRect);
            redraw(windowGroup);

            println("2. Moving blue to (80, 0), others shouldn't move");
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

            println("4. Changing constraint on x of red rect to be next to black");
            println("   (Test: replace the original constraint with a new one)");
            pause();
            redRect.setX(new Constraint<Integer>(blackRect.useX()) {
                public Integer getValue() {
                    return blackRect.getX() + 50;
                }
            });
            redraw(windowGroup);

            println("5. Changing constraint on x of red rect to be x of blue rect divided by zero");
            println("   (Test: crashed user implementation)");
            pause();
            redRect.setX(new Constraint<Integer>(blueRect.useX()) {
                public Integer getValue() {
                    return blueRect.getX() / 0;
                }
            });
            redraw(windowGroup);

            println("6. Changing constraint on x of red rect to be next to the leftmost rect");
            println("   (Test: constraint depending on two objects)");
            pause();
            redRect.setX(new Constraint<Integer>(
                blueRect.useX(), blackRect.useX()
            ) {
                public Integer getValue() {
                    return Math.min(blueRect.getX(), blackRect.getX()) + 50;
                }
            });
            redraw(windowGroup);

            println("7. Switching positions of blue and black rects");
            println("   Red rect should not move");
            pause();
            int blackX = blackRect.getX(), blueX = blueRect.getX();
            blueRect.setX(blackX);
            blackRect.setX(blueX);
            redraw(windowGroup);

            println("8. Constraint on x and y of red rect to be at bottom-right of blue rect");
            println("   (Test: two constraints imposed on different attributes)");
            pause();
            redRect.setX(new Constraint<Integer>("redRect.x", blueRect.useX()) {
                public Integer getValue() {
                    return blueRect.getX() + 50;
                }
            });
            redRect.setY(new Constraint<Integer>(blueRect.useY()) {
                public Integer getValue() {
                    return blueRect.getY() + 80;
                }
            });
            redraw(windowGroup);

            println("9. Constraint on x and y of black rect to be at bottom-right of red rect");
            println("   (Test: chain of constraints)");
            pause();
            blackRect.setX(new Constraint<Integer>(redRect.useX()) {
                public Integer getValue() {
                    return redRect.getX() + 50;
                }
            });
            blackRect.setY(new Constraint<Integer>(redRect.useY()) {
                public Integer getValue() {
                    return redRect.getY() + 80;
                }
            });
            redraw(windowGroup);

            println("10. Moving blue rect to (50, 80)");
            println("    Red and black rects should follow");
            pause();
            blueRect.moveTo(50, 80);
            redraw(windowGroup);

            println("11. Moving red rect to (150, 240) where black rect is at");
            println("    Should not move");
            println("    (Test: set value on a constrained attribute)");
            pause();
            redRect.moveTo(150, 240);
            redraw(windowGroup);

            println("12. Adding constraint on blue rect to be at top-left of red rect");
            println("    (Test: valid cycle in dependency graph)");
            pause();
            blueRect.setX(new Constraint<Integer>("blueRect.x", redRect.useX()) {
                public Integer getValue() {
                    return redRect.getX() - 50;
                }
            });
            blueRect.setY(new Constraint<Integer>(redRect.useY()) {
                public Integer getValue() {
                    return redRect.getY() - 80;
                }
            });
            redraw(windowGroup);

            println("13. Moving red rect to (150, 240) again");
            println("    (Test: multi-way constraint in case of cycle)");
            pause();
            redRect.moveTo(150, 240);
            redraw(windowGroup);

            println("14. Changing constraint on blue rect to be at bottom-right of red rect");
            println("    (Test: invalid cycle in dependency graph)");
            pause();
            blueRect.setX(new Constraint<Integer>("blueRect.x", redRect.useX()) {
                public Integer getValue() {
                    return redRect.getX() + 50;
                }
            });
            blueRect.setY(new Constraint<Integer>(redRect.useY()) {
                public Integer getValue() {
                    return redRect.getY() + 80;
                }
            });
            redraw(windowGroup);

            println("14. Constraint on variables of more than one type");
            pause();
            println("15. Constraint on subclass object");
            println("16. Stress test");

            println("============= Basic Tests End =============");
            println("============= Advanced Tests =============");

            println("============= Advanced Tests End =============");

            println("DONE! Close the window to exit");

        } catch (Exception e) {
            println("An exception occurred: " + e);
        }
    }

    private void debug(OutlineRect dependency, OutlineRect target) {
        System.out.println(target.useX().getInEdges().get(0).getStart());
        System.out.println(target.useX().getInEdges().get(0).getEnd());
        System.out.println(target.useX().getInEdges().get(0).isPending());
        System.out.println(target.useX().isOutOfDate());
        System.out.println(dependency.useX().isOutOfDate());
    }
}