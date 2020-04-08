package widget;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import constraint.Constraint;
import constraint.SetupConstraint;
import graphics.object.BoundaryRectangle;
import graphics.object.GraphicalObject;
import graphics.object.Text;
import graphics.object.selectable.SelectableOutlineRect;

public class CheckBox extends SelectableOutlineRect {
    private Text indicator;
    private GraphicalObject label;

    public CheckBox(GraphicalObject label) {
        super();
        this.label = label;

        int boxSize = 14;
        int indicatorSize = 10;
        this.setWidth(boxSize);
        this.setHeight(boxSize);
        indicator = new Text("✔");
        indicator.setColor(Color.WHITE);
        setupAlignment(this, indicator, label);
    }

    public CheckBox(String label) {
        this(new Text(label));
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

        if (!labelType.equals("Text")) {
            label.setY(new Constraint<Integer>(option.useY()) {
                public Integer getValue() {
                    return option.getY();
                }
            });
        } else {
            Text textLabel = (Text) label;
            textLabel.setY(new Constraint<Integer>(option.useY()) {
                public Integer getValue() {
                    return option.getY() + textLabel.getAscent();
                }
            });
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