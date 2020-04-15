package widget;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import constraint.Constraint;
import constraint.SetupConstraint;
import graphics.object.BoundaryRectangle;
import graphics.object.GraphicalObject;
import graphics.object.Line;
import graphics.object.Text;
import graphics.object.selectable.SelectableOutlineRect;

public class CheckBox extends SelectableOutlineRect {
    private Text indicator;
    private GraphicalObject label;

    public CheckBox(int x, int y, GraphicalObject label) {
        super(x, y, 0, 0, Color.BLACK, 1);
        this.label = label;

        int boxSize = 14;
        this.setWidth(boxSize);
        this.setHeight(boxSize);
        indicator = new Text("✔");
        indicator.setColor(Color.WHITE);
        setupAlignment(this, indicator, label);
    }

    public CheckBox(GraphicalObject label) {
        this(0, 0, label);
    }

    public CheckBox(String label) {
        this(new Text(label));
    }

    public CheckBox() {
        this("Label");
    }

    private void setupAlignment(SelectableOutlineRect option, Text indicator, GraphicalObject label) {
        indicator.setX(new Constraint<Integer>(option.useX()) {
            public Integer getValue() {
                return option.getX() + (option.getWidth() - indicator.getWidth()) / 2;
            }
        });

        indicator.setY(new Constraint<Integer>(option.useY()) {
            public Integer getValue() {
                return option.getY() + (option.getHeight() - indicator.getHeight()) / 2 + indicator.getAscent();
            }
        });

        label.setX(new Constraint<Integer>(option.useX()) {
            public Integer getValue() {
                return option.getX() + option.getWidth() * 2;
            }
        });

        String labelType = label.getClass().getSimpleName();

        switch (labelType) {
            case "Text": {
                Text textLabel = (Text) label;
                textLabel.setY(new Constraint<Integer>(option.useY()) {
                    public Integer getValue() {
                        return option.getY() + textLabel.getAscent();
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
                lineLabel.setY(new Constraint<Integer>(option.useY()) {
                    public Integer getValue() {
                        return option.getY() + lineLabel.getLineThickness();
                    }
                });
            }
            default: {
                label.setY(new Constraint<Integer>(option.useY()) {
                    public Integer getValue() {
                        return option.getY();
                    }
                });
            }
        }
    }

    public CheckBox setAlignment(SetupConstraint constraint, GraphicalObject... objects) {
        constraint.setup(objects);
        return this;
    }

    public SelectableOutlineRect getOption() {
        return this;
    }

    public Text getIndicator() {
        return this.indicator;
    }

    public GraphicalObject getLabel() {
        return this.label;
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        super.draw(graphics, clipShape);
        indicator.draw(graphics, clipShape);
        label.draw(graphics, clipShape);
    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        BoundaryRectangle r = label.getBoundingBox();
        return new BoundaryRectangle(
            getX(), getY(),
            getWidth() * 2 + r.getWidth(),
            Math.max(getHeight(), r.getHeight())
        );
    }
}