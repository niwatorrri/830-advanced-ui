import java.awt.*;
import java.util.Arrays;
import java.io.IOException;

public class TestHomework3 extends TestFrame {
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 400;

    public static void main(String[] args) {
        new TestHomework3(args);
    }

    public TestHomework3(String[] args) {
        super("TestHomework3", FRAME_WIDTH, FRAME_HEIGHT);

        try {
            final int DEFAULT_RECT_WIDTH = 50;
            final int DEFAULT_RECT_HEIGHT = 80;

            println("1. Creating blue, red and black rects");
            OutlineRect blueRect = new OutlineRect(0, 0,
                DEFAULT_RECT_WIDTH, DEFAULT_RECT_HEIGHT, Color.blue, 5);
            OutlineRect redRect = new OutlineRect(100, 0,
                DEFAULT_RECT_WIDTH, DEFAULT_RECT_HEIGHT, Color.red, 1);
            OutlineRect blackRect = new OutlineRect(250, 0,
                DEFAULT_RECT_WIDTH, DEFAULT_RECT_HEIGHT, Color.black, 3);

            Group windowGroup = new SimpleGroup(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
            Group group = new SimpleGroup(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
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
                    return blueRect.getX() + DEFAULT_RECT_WIDTH;
                }
            });
            redraw(windowGroup);

            println("4. Changing constraint on x of red rect to be next to black");
            println("   (Test: replace the original constraint with a new one)");
            pause();
            redRect.setX(new Constraint<Integer>(blackRect.useX()) {
                public Integer getValue() {
                    return blackRect.getX() + DEFAULT_RECT_WIDTH;
                }
            });
            redraw(windowGroup);

            println("5. Changing constraint on x of red rect to be x of blue rect divided by 0");
            println("   (Test: crashed user implementation)");
            pause();
            redRect.setX(new Constraint<Integer>("redRect.x", blueRect.useX()) {
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
                    return Math.min(blueRect.getX(), blackRect.getX()) + DEFAULT_RECT_WIDTH;
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
            redRect.setX(new Constraint<Integer>(
                blueRect.useX(), blueRect.useWidth()
            ) {
                public Integer getValue() {
                    return blueRect.getX() + blueRect.getWidth();
                }
            });
            redRect.setY(new Constraint<Integer>(
                blueRect.useY(), blueRect.useHeight()
            ) {
                public Integer getValue() {
                    return blueRect.getY() + blueRect.getHeight();
                }
            });
            redraw(windowGroup);

            println("9. Constraint on x and y of black rect to be at bottom-right of red rect");
            println("   (Test: chain of constraints)");
            pause();
            blackRect.setX(new Constraint<Integer>(
                redRect.useX(), redRect.useWidth()
            ) {
                public Integer getValue() {
                    return redRect.getX() + redRect.getWidth();
                }
            });
            blackRect.setY(new Constraint<Integer>(
                redRect.useY(), redRect.useHeight()
            ) {
                public Integer getValue() {
                    return redRect.getY() + redRect.getHeight();
                }
            });
            redraw(windowGroup);

            println("10. Moving blue rect to (50, 80)");
            println("    Red and black rects should follow");
            pause();
            blueRect.moveTo(DEFAULT_RECT_WIDTH, DEFAULT_RECT_HEIGHT);
            redraw(windowGroup);

            println("11. Moving red rect to (50, 80) where blue rect is at");
            println("    Red should not move");
            println("    (Test: set value on a constrained attribute)");
            pause();
            redRect.moveTo(DEFAULT_RECT_WIDTH, DEFAULT_RECT_HEIGHT);
            redraw(windowGroup);

            println("12. Adding constraint on blue rect to be at top-left of red rect");
            println("    (Test: valid cycle in dependency graph)");
            pause();
            blueRect.setX(new Constraint<Integer>("blueRect.x", redRect.useX()) {
                public Integer getValue() {
                    return redRect.getX() - DEFAULT_RECT_WIDTH;
                }
            });
            blueRect.setY(new Constraint<Integer>("blueRect.y", redRect.useY()) {
                public Integer getValue() {
                    return redRect.getY() - DEFAULT_RECT_HEIGHT;
                }
            });
            redraw(windowGroup);

            println("13. Moving red rect to (50, 80) again");
            println("    (Test: multi-way constraint in case of cycle)");
            pause();
            redRect.moveTo(DEFAULT_RECT_WIDTH, DEFAULT_RECT_HEIGHT);
            redraw(windowGroup);

            println("14. Changing constraint on blue rect to be at bottom-right of red rect");
            println("    (Test: invalid cycle in dependency graph)");
            pause();
            blueRect.setX(new Constraint<Integer>("blueRect.x", redRect.useX()) {
                public Integer getValue() {
                    return redRect.getX() + DEFAULT_RECT_WIDTH;
                }
            });
            blueRect.setY(new Constraint<Integer>("blueRect.y", redRect.useY()) {
                public Integer getValue() {
                    return redRect.getY() + DEFAULT_RECT_HEIGHT;
                }
            });
            redraw(windowGroup);

            println("   Removing invalid constraints on blue rect");
            blueRect.setX(new Constraint<Integer>());
            blueRect.setY(new Constraint<Integer>());

            println("15. Adding constraint on width, height and line thickness of red rect");
            println("    to be twice as that of blue rect");
            pause();
            redRect.setWidth(new Constraint<Integer>(blueRect.useWidth()) {
                public Integer getValue() {
                    return blueRect.getWidth() * 2;
                }
            });
            redRect.setHeight(new Constraint<Integer>(blueRect.useHeight()) {
                public Integer getValue() {
                    return blueRect.getHeight() * 2;
                }
            });
            redRect.setLineThickness(new Constraint<Integer>(blueRect.useLineThickness()) {
                public Integer getValue() {
                    return blueRect.getLineThickness() * 2;
                }
            });
            redraw(windowGroup);

            println("16. Creating two green filled rects");
            println("    (Test: filled rect objects)");
            pause();
            FilledRect filledRect1 = new FilledRect(200, 80,
                DEFAULT_RECT_WIDTH, DEFAULT_RECT_HEIGHT, Color.green);
            FilledRect filledRect2 = new FilledRect(300, 80,
                DEFAULT_RECT_WIDTH, DEFAULT_RECT_HEIGHT, Color.green);
            group.addChild(filledRect1);
            group.addChild(filledRect2);
            redraw(windowGroup);

            println("17. Constraint on one new rect to have the same color as blue rect");
            println("    and on another to have a darker blue color");
            println("    (Test: Constraint on colors rather than ints)");
            pause();
            filledRect1.setColor(new Constraint<Color>(blueRect.useColor()) {
                public Color getValue() {
                    return blueRect.getColor();
                }
            });
            filledRect2.setColor(new Constraint<Color>(filledRect1.useColor()) {
                public Color getValue() {
                    return filledRect1.getColor().darker();
                }
            });
            redraw(windowGroup);

            println("18. Changing blue outline rect to magenta");
            println("    Filled rects should change to magenta as well");
            pause();
            blueRect.setColor(Color.magenta);
            redraw(windowGroup);

            println("19. Creating a black line");
            println("    (Test: line objects)");
            pause();
            Line line = new Line(250, 280, 300, 280, Color.black, 2);
            group.addChild(line);
            redraw(windowGroup);

            println("20. Constraint on the line to connect filled rects by their centers");
            pause();
            // should have provided easier API for this
            line.setX1(new Constraint<Integer>(filledRect1.useX(), filledRect1.useWidth()) {
                public Integer getValue() {
                    return filledRect1.getX() + filledRect1.getWidth() / 2;
                }
            });
            line.setY1(new Constraint<Integer>(filledRect1.useY(), filledRect1.useHeight()) {
                public Integer getValue() {
                    return filledRect1.getY() + filledRect1.getHeight() / 2;
                }
            });
            line.setX2(new Constraint<Integer>(filledRect2.useX(), filledRect2.useWidth()) {
                public Integer getValue() {
                    return filledRect2.getX() + filledRect2.getWidth() / 2;
                }
            });
            line.setY2(new Constraint<Integer>(filledRect2.useY(), filledRect2.useHeight()) {
                public Integer getValue() {
                    return filledRect2.getY() + filledRect2.getHeight() / 2;
                }
            });
            redraw(windowGroup);

            println("21. Moving the brighter filled rect up and the darker one down");
            pause();
            filledRect1.moveTo(200, 0);
            filledRect2.moveTo(300, 160);
            redraw(windowGroup);

            println("22. Creating text that specifies an RGB '60,20,240'");
            println("    (Test: text objects)");
            pause();
            Graphics2D g = (Graphics2D) buffer.getGraphics();
            Text text = new Text(g, "60,20,240", 250, 260,
                new Font("Monospaced", Font.PLAIN, 14), Color.black);
            group.addChild(text);
            redraw(windowGroup);

            println("23. Constraint on the top-left outline rect to have " +
                        "the text-specified RGB color");
            println("    (Test: example of arbitrary code in constraints)");
            pause();
            blueRect.setColor(new Constraint<Color>(text.useText()) {
                public Color getValue() {
                    int[] rgb = Arrays.stream(text.getText().split(","))
                                      .mapToInt(Integer::parseInt).toArray();
                    return new Color(rgb[0], rgb[1], rgb[2]);
                }
            });
            redraw(windowGroup);

            println("24. Constraint on the text to have the RGB color specified by itself");
            println("    (Test: constraint that depends on attributes of itself)");
            pause();
            text.setColor(new Constraint<Color>(text.useText()) {
                public Color getValue() {
                    int[] rgb = Arrays.stream(text.getText().split(","))
                                      .mapToInt(Integer::parseInt).toArray();
                    return new Color(rgb[0], rgb[1], rgb[2]);
                }
            });
            redraw(windowGroup);

            println("25. Creating an icon object to load images constrained by");
            println("    the file name specified by another text object");
            pause();
            Text fileName = new Text(g, "dog.gif", 250, 290,
                new Font("Monospaced", Font.PLAIN, 14), Color.black);
            Icon image = new Icon(null, 20, 20);
            group.addChild(fileName);
            group.addChild(image);
            redraw(windowGroup);

            println("    Begin loading image");
            pause();
            image.setImage(new Constraint<Image>(fileName.useText()) {
                public Image getValue() {
                    String file = fileName.getText();
                    try {
                        Image loadedImage = loadImageFully(file);
                        return loadedImage;
                    } catch (IOException e) {
                        println("Failed to load" + file);
                        return null;
                    }
                }
            });
            redraw(windowGroup);

            println("    Removing loaded image");
            pause();
            group.removeChild(image);

            println("26. Constraint on subclass object");
            pause();
            println("Constraint on groups");
            println("Stress test");

            println("DONE! Close the window to exit");

        } catch (Exception e) {
            println("An exception occurred: " + e);
        }
    }
}