package widget;

import java.awt.Color;

import behavior.BehaviorEvent;
import behavior.ChoiceBehavior;
import behavior.MoveBehavior;
import constraint.Constraint;
import constraint.NoConstraint;
import constraint.SetupConstraint;
import graphics.group.SimpleGroup;
import graphics.object.FilledEllipse;
import graphics.object.FilledRect;
import graphics.object.Text;
import graphics.object.selectable.SelectableFilledEllipse;
import graphics.object.selectable.SelectableGraphicalObject;

public class NumberSlider extends Widget {
    private FilledRect bar;
    private FilledEllipse slider;
    private SelectableFilledEllipse incrementButton, decrementButton;
    private Text valueDisplay;

    private int value;
    private Constraint<Integer> valueConstraint = new NoConstraint<>();

    public NumberSlider(int x, int y, int minValue, int maxValue, int increment, int layout) {
        this.value = (minValue + maxValue) / 2;

        int barLength = 100;
        int sliderRadius = 8;
        int buttonSize = 8;
        this.widget = new SimpleGroup(x, y, barLength + 8 * buttonSize, 2 * sliderRadius).addChildren(
            // the bar
            bar = (layout == HORIZONTAL_LAYOUT) ? 
                new FilledRect(2 * buttonSize, sliderRadius, barLength, 3, Color.BLACK) :
                new FilledRect(sliderRadius, 2 * buttonSize, 3, barLength, Color.BLACK),

            // the slider
            new SimpleGroup(0, 0, barLength + 4 * buttonSize, 2 * sliderRadius)
                .addChild(slider = new LimitedFilledEllipse(
                    0, 0, 2 * sliderRadius, 2 * sliderRadius, Color.LIGHT_GRAY,
                    bar.getX() - sliderRadius,
                    bar.getX() + bar.getWidth() - sliderRadius
                ))
                .addBehavior(new MoveBehavior()),

            // the increment/decrement buttons
            new SimpleGroup()
                .addChildren(
                    incrementButton = new SelectableFilledEllipse(
                        3 * buttonSize + barLength, sliderRadius - buttonSize / 2,
                        buttonSize, buttonSize, Color.LIGHT_GRAY
                    ),
                    decrementButton = new SelectableFilledEllipse(
                        0, sliderRadius - buttonSize / 2,
                        buttonSize, buttonSize, Color.LIGHT_GRAY
                    )
                )
                .resizeToChildren()
                .addBehavior(new ChoiceBehavior(ChoiceBehavior.SINGLE, true) {
                    @Override
                    public boolean stop(BehaviorEvent event) {
                        boolean eventConsumed = super.stop(event);
                        if (eventConsumed) {
                            SelectableGraphicalObject buttonInvolved = getSelection().get(0);
                            if (buttonInvolved.isSelected()) {
                                int sign = (buttonInvolved == incrementButton) ? 1 : -1;
                                int newValue = getValue() + sign * increment;
                                newValue = Math.min(maxValue, Math.max(minValue, newValue));
                                setValue(newValue);
                            }
                        }
                        return eventConsumed;
                    }
                }),

            // the text to show current value
            valueDisplay = new Text(String.valueOf(maxValue), 6 * buttonSize + barLength, 0)
        );
        valueDisplay.setY(bar.getY() + valueDisplay.getAscent() / 2);
        widget.resizeToChildren();

        // Set up a bunch of constraints
        // 1. slider should not leave the bar
        slider.setY(new Constraint<Integer>(bar.useY()) {
            public Integer getValue() {
                return bar.getY() - slider.getHeight() / 2;
            }
        });

        // 2. slider position should align with current value
        slider.setX(new Constraint<Integer>(this.useValue()) {
            public Integer getValue() {
                int currentValue = getThis().getValue();
                double proportion = (double) (currentValue - minValue) / (maxValue - minValue);
                proportion = Math.min(1.0, Math.max(0.0, proportion));
                return bar.getX() + (int) (bar.getWidth() * proportion) - slider.getWidth() / 2;
            }
        });
        this.setValue(new Constraint<Integer>(slider.useX()) {
            public Integer getValue() {
                int currentX = slider.getX() + slider.getWidth() / 2;
                double proportion = (double) (currentX - bar.getX()) / bar.getWidth();
                proportion = Math.min(1.0, Math.max(0.0, proportion));
                return minValue + (int) ((maxValue - minValue) * proportion);
            }
        });
        this.useValue().setOutOfDate(false);

        // 3. value display should reflect current value
        valueDisplay.setText(new Constraint<String>("value.text", this.useValue()) {
            public String getValue() {
                return String.valueOf(getThis().getValue());
            }
        });

        // 4. increment/decrement buttons should change color upon clicks
        SetupConstraint clickedColor = o -> {
            SelectableFilledEllipse r = (SelectableFilledEllipse) o[0];
            r.setColor(new Constraint<Color>(r.useInterimSelected()) {
                public Color getValue() {
                    return r.isInterimSelected() ? Color.GREEN : Color.BLACK;
                }
            });
        };
        clickedColor.setup(incrementButton);
        clickedColor.setup(decrementButton);
    }

    public NumberSlider(int x, int y) {
        this(x, y, 0, 100, 10, HORIZONTAL_LAYOUT);
    }

    private class LimitedFilledEllipse extends FilledEllipse {
        private int lower, upper;

        public LimitedFilledEllipse(int x, int y, int width, int height, Color color,
                int lower, int upper) {
            super(x, y, width, height, color);
            this.lower = lower;
            this.upper = upper;
        }

        @Override
        public void setX(int x) {
            int clippedX = Math.min(upper, Math.max(lower, x));
            super.setX(clippedX);
        }
    }

    private NumberSlider getThis() {
        return this;
    }

    /**
     * Getters for components
     */
    public FilledRect getBar() {
        return this.bar;
    }

    public FilledEllipse getSlider() {
        return this.slider;
    }

    public SelectableFilledEllipse getIncrementButton() {
        return this.incrementButton;
    }

    public SelectableFilledEllipse getDecrementButton() {
        return this.decrementButton;
    }

    public Text getValueDisplay() {
        return this.valueDisplay;
    }

    /**
     * Getter, setter and "user" for value
     */
    public int getValue() {
        if (valueConstraint.isConstrained()) {
            this.value = valueConstraint.evaluate();
        }
        return this.value;
    }

    public void setValue(int value) {
        if (this.value != value) {
            if (!valueConstraint.isConstrained()) {
                this.value = value;
                valueConstraint.notifyValueChange(false);
            } else if (valueConstraint.hasCycle()) {
                valueConstraint.setValue(value);
                valueConstraint.notifyValueChange(false);
            }
        }
    }

    public void setValue(Constraint<Integer> constraint) {
        valueConstraint.replaceWithConstraint(constraint);
        valueConstraint = constraint;
        valueConstraint.setValue(this.value);
        valueConstraint.notifyValueChange(true);
    }

    public Constraint<Integer> useValue() {
        return this.valueConstraint;
    }
}