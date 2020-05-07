package ui.talk;

import ui.toolkit.graphics.object.GraphicalObject;
import ui.toolkit.graphics.object.Rect;

import java.awt.*;

public class InteractionOutcome {

    public GraphicalObject target;
    public String property;
    public String value;

    public InteractionOutcome(GraphicalObject target, String property, String value) {
        this.target = target;
        this.property = property;
        this.value = value;
    }

    public void apply() {

        // apply the outcome for setting the color property for a variety of objects
        switch (property.toLowerCase()) {
            case "color": {
                Color c = Entity.stringToColor.get(value);
                if (c != null) {
                    if (target instanceof Rect) {
                        ((Rect) target).setColor(c);
                    }
                }
                break;
            }

            default: {
                System.out.println("For demo, only color outcomes have been implemented");
                break;
            }
        }
    }
}