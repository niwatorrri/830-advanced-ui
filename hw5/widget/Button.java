package widget;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import constraint.Constraint;
import constraint.SetupConstraint;
import graphics.object.BoundaryRectangle;
import graphics.object.GraphicalObject;
import graphics.object.Text;
import graphics.object.selectable.SelectableFilledRect;

public class Button extends SelectableFilledRect {
    private GraphicalObject label;

    public Button(GraphicalObject label) {
        super();
        this.label = label;

        BoundaryRectangle r = label.getBoundingBox();
        this.setWidth((int) (1.5 * r.width));
        this.setHeight((int) (1.5 * r.height));
        this.setColor(Color.LIGHT_GRAY);
        setupAlignment(this, label);
    }

    public Button(String label) {
        this(new Text(label));
    }

    private void setupAlignment(SelectableFilledRect box, GraphicalObject label) {
        String labelType = label.getClass().getSimpleName();

        label.setX(new Constraint<Integer>(box.useX()) {
            public Integer getValue() {
                return box.getX();
            }
        });

        if (!labelType.equals("Text")) {
            label.setY(new Constraint<Integer>(box.useY()) {
                public Integer getValue() {
                    return box.getY();
                }
            });
        } else {
            Text textLabel = (Text) label;
            textLabel.setY(new Constraint<Integer>(box.useY()) {
                public Integer getValue() {
                    return box.getY() + textLabel.getAscent();
                }
            });
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