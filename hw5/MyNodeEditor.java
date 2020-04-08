import java.awt.Color;

import behavior.*;
import constraint.*;
import graphics.group.*;
import graphics.object.*;
import widget.*;

public class MyNodeEditor extends InteractiveWindowGroup {
    /**
     * Re-implementing the node editor using our own toolkit
     */
    private static final long serialVersionUID = 1L;

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 400;
    private static final int SEPARATION_LEFT = 200;
    private static final int SEPARATION_RIGHT = WINDOW_WIDTH - SEPARATION_LEFT;

    public static void main(String[] args) {
        new MyNodeEditor();
    }

    public MyNodeEditor() {
        super("My Node Editor", WINDOW_WIDTH, WINDOW_HEIGHT);

        Text lineColorText = new Text("Choose a line color");
        RadioButtonPanel lineColors =
            new RadioButtonPanel()
                .addChildren(
                    new RadioButton(new Line(0, 10, 40, 10, Color.BLACK, 3)),
                    new RadioButton(new Line(0, 10, 40, 10, Color.BLUE, 3)),
                    new RadioButton(new Line(0, 10, 40, 10, Color.MAGENTA, 3)),
                    new RadioButton(new Line(0, 10, 40, 10, Color.CYAN, 3))
                )
                .setSelection("one");

        Text boxStyleText = new Text("Choose box style");
        CheckBoxPanel boxStyles =
            new CheckBoxPanel()
                .addChildren(
                    new CheckBox("Outline box"),
                    new CheckBox("Filled box")
                )
                .setSelection("all");

        Text lineThicknessText = new Text("Choose line thickness");
        NumberSlider lineThicknessSlider =
            new NumberSlider(0, 0, 1, 9, 1, NumberSlider.HORIZONTAL_LAYOUT);
            
        Line separationLine = new Line(
            SEPARATION_LEFT, 0, SEPARATION_LEFT, WINDOW_HEIGHT, Color.BLACK, 2
        );

        SetupConstraint selectionConstraint = o -> {
            Box r = (Box) o[0];
            r.getFill().setColor(new Constraint<Color>(r.useSelected()) {
                public Color getValue() {
                    return r.isSelected() ? Color.LIGHT_GRAY : Color.WHITE;
                }
            });
        };

        Group drawingPanel =
            new SimpleGroup(SEPARATION_LEFT, 0, SEPARATION_RIGHT, WINDOW_HEIGHT)
                .addBehaviors(
                    new MoveBehavior(),
                    new ChoiceBehavior(ChoiceBehavior.SINGLE, false) {
                        @Override
                        public boolean stop(BehaviorEvent event) {
                            boolean eventConsumed = super.stop(event);
                            if (eventConsumed) {
                                Box b = (Box) getSelection().get(0);
                                lineThicknessSlider.setValue(b.getLineThickness());
                                for (GraphicalObject o : lineColors.getChildren()) {
                                    RadioButton rb = (RadioButton) o;
                                    rb.setSelected(((Line) rb.getLabel()).getColor().equals(b.getColor()));
                                }
                            }
                            return eventConsumed;
                        }
                    },
                    new NewBoxBehavior(
                            NewBoxBehavior.OUTLINE_RECT, Color.BLACK, 1,
                            selectionConstraint
                        )
                        .setLineThickness(new Constraint<Integer>(lineThicknessSlider.useValue()) {
                            public Integer getValue() {
                                return lineThicknessSlider.getValue();
                            }
                        })
                        .setColor(new Constraint<Color>(lineColors.useValue()) {
                            public Color getValue() {
                                return ((Line) lineColors.getValue().getLabel()).getColor();
                            }
                        })
                        .setType(new Constraint<Integer>(boxStyles.useValue()) {
                            public Integer getValue() {
                                int outline = ((CheckBox) boxStyles.getChildren().get(0)).isSelected() ? Box.OUTLINE : 0;
                                int filled = ((CheckBox) boxStyles.getChildren().get(1)).isSelected() ? Box.FILLED : 0;
                                return outline + filled;
                            }
                        })
                        .setPriority(1)
                );

        Text deleteText = new Text("Delete a selected box");
        Widget<?> deleteButton =
            new ButtonPanel(0, 0, false, ButtonPanel.MULTIPLE)
                .addChild(new Button("Delete"))
                .setCallback(o -> {
                    for (GraphicalObject child : drawingPanel.getChildren()) {
                        if (((Box) child).isSelected()) {
                            drawingPanel.removeChild(child);
                        }
                    }
                });

        Group selectionPanel =
            new LayoutGroup(20, 20, SEPARATION_LEFT, WINDOW_HEIGHT, 
                        LayoutGroup.VERTICAL, 20)
                .addChildren(
                    lineColorText, lineColors, boxStyleText, boxStyles,
                    lineThicknessText, lineThicknessSlider, deleteText, deleteButton
                );
        
        addChildren(selectionPanel, separationLine, drawingPanel);
    }
}