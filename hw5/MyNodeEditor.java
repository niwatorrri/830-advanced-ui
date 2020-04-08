import java.awt.Color;

import behavior.*;
import constraint.*;
import graphics.group.*;
import graphics.object.*;
import widget.*;

public class MyNodeEditor extends InteractiveWindowGroup {
    /**
     * An example interactive window that allows users to make new rectangles,
     * move them by click and drag, and choose them with a single choice behavior
     */
    private static final long serialVersionUID = 1L;

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    private static final int SEPARATION_LEFT = 150;
    private static final int SEPARATION_RIGHT = WINDOW_WIDTH - SEPARATION_LEFT;

    public static void main(String[] args) {
        new MyNodeEditor();
    }

    public MyNodeEditor() {
        super("My Node Editor", WINDOW_WIDTH, WINDOW_HEIGHT);

        SetupConstraint selectionConstraint = o -> {
            Box r = (Box) o[0];
            r.getFill().setColor(new Constraint<Color>(r.useSelected()) {
                public Color getValue() {
                    return r.isSelected() ? Color.GREEN.brighter() : Color.WHITE;
                }
            });
        };

        Widget<RadioButton> lineStyles =
            new RadioButtonPanel(30, 50)
                .addChildren(
                    new RadioButton(new Line(0, 10, 40, 10, Color.BLACK, 1)),
                    new RadioButton(new Line(0, 10, 40, 10, Color.BLACK, 2)),
                    new RadioButton(new Line(0, 10, 40, 10, Color.BLACK, 3)),
                    new RadioButton(new Line(0, 10, 40, 10, Color.BLACK, 4))
                )
                .setDefaultSelection();

        Line separationLine = new Line(
            SEPARATION_LEFT, 0, SEPARATION_LEFT, WINDOW_HEIGHT, Color.BLACK, 2
        );

        Group drawingPanel =
            new SimpleGroup(SEPARATION_LEFT, 0, SEPARATION_RIGHT, WINDOW_HEIGHT)
                .addBehaviors(
                    new MoveBehavior(),
                    new ChoiceBehavior(ChoiceBehavior.SINGLE, false) {
                        @Override
                        public boolean stop(BehaviorEvent event) {
                            boolean eventConsumed = super.stop(event);
                            if (eventConsumed) {
                                int lt = ((Box) getSelection().get(0)).getLineThickness();
                                for (GraphicalObject o : lineStyles.getChildren()) {
                                    RadioButton rb = (RadioButton) o;
                                    rb.setSelected(((Line) rb.getLabel()).getLineThickness() == lt);
                                }
                            }
                            return eventConsumed;
                        }
                    },
                    new NewBoxBehavior(
                            NewBoxBehavior.OUTLINE_RECT, Color.BLACK, 1,
                            selectionConstraint
                        )
                        .setLineThickness(new Constraint<Integer>(lineStyles.useValue()) {
                            public Integer getValue() {
                                return ((Line) lineStyles.getValue().getLabel()).getLineThickness();
                            }
                        })
                        .setPriority(1)
                );

        Widget<?> deleteButton =
            new ButtonPanel(30, 200, false, ButtonPanel.MULTIPLE)
                .addChild(new Button("Delete"))
                .setCallback(o -> {
                    for (GraphicalObject child : drawingPanel.getChildren()) {
                        if (((Box) child).isSelected()) {
                            drawingPanel.removeChild(child);
                        }
                    }
                });

        Group selectionPanel =
            new SimpleGroup(0, 0, SEPARATION_LEFT, WINDOW_HEIGHT)
                .addChildren(lineStyles, deleteButton);
        
        addChildren(selectionPanel, separationLine, drawingPanel);
    }

    public void testWidgets() {
        addChildren(
            new ButtonPanel().addChildren(
                new Button(new Ellipse()),
                new Button("Test"),
                new Button("What???\nthe fuck???"),
                new Button(new FilledRect())
            ),
            new NumberSlider(200, 50),
            new RadioButtonPanel(200, 200).addChildren(
                new RadioButton("Test"),
                new RadioButton(new Ellipse())
            ),
            new CheckBoxPanel(100, 200).addChildren(
                new CheckBox("test"),
                new CheckBox(new FilledRect(0, 0, 20, 30, Color.GREEN))
            )
        );
    }
}