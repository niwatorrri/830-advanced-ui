package ui.toolkit.widget;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import ui.toolkit.constraint.Constraint;
import ui.toolkit.constraint.SetupConstraint;
import ui.toolkit.graphics.object.BoundaryRectangle;
import ui.toolkit.graphics.object.GraphicalObject;
import ui.toolkit.graphics.object.Line;
import ui.toolkit.graphics.object.Text;
import ui.toolkit.graphics.object.selectable.SelectableFilledRect;

public class Button extends SelectableFilledRect {
    private GraphicalObject label;

    public Button(int x, int y, GraphicalObject label) {
        super(x, y, 0, 0, null);
        this.label = label;

        BoundaryRectangle r = label.getBoundingBox();
        this.setWidth((int) (1.5 * r.width));
        this.setHeight((int) (1.5 * r.height));
        this.setColor(Color.LIGHT_GRAY);
        setupAlignment(this, label);
    }

    public Button(GraphicalObject label) {
        this(0, 0, label);
    }

    public Button(String label) {
        this(new Text(label));
    }

    public Button() {
        this("Label");
    }

    private void setupAlignment(SelectableFilledRect box, GraphicalObject label) {
        String labelType = label.getClass().getSimpleName();

        label.setX(new Constraint<Integer>(box.useX()) {
            public Integer getValue() {
                return box.getX();
            }
        });

        switch (labelType) {
            case "Text": {
                Text textLabel = (Text) label;
                textLabel.setY(new Constraint<Integer>(box.useY()) {
                    public Integer getValue() {
                        return box.getY() + textLabel.getAscent();
                    }
                });
                break;
            }
            case "Line": {
                Line lineLabel = (Line) label;
                lineLabel.setInvariant(true);
                lineLabel.setX2(new Constraint<Integer>(lineLabel.useX1()) {
                    public Integer getValue() {
                        return lineLabel.getX1() + lineLabel.getDx();
                    }
                });
                lineLabel.setY2(new Constraint<Integer>(lineLabel.useY1()) {
                    public Integer getValue() {
                        return lineLabel.getY1() + lineLabel.getDy();
                    }
                });
                lineLabel.setY(new Constraint<Integer>(box.useY()) {
                    public Integer getValue() {
                        return box.getY() + lineLabel.getLineThickness();
                    }
                });
                break;
            }
            default: {
                label.setY(new Constraint<Integer>(box.useY()) {
                    public Integer getValue() {
                        return box.getY();
                    }
                });
            }
        }
    }

    public Button setAlignment(SetupConstraint constraint, GraphicalObject... objects) {
        constraint.setup(objects);
        return this;
    }

    public SelectableFilledRect getBox() {
        return this;
    }

    public GraphicalObject getLabel() {
        return this.label;
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        super.draw(graphics, clipShape);
        label.draw(graphics, clipShape);
    }
}