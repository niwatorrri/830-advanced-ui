package ui.talk;

import java.awt.Color;
import java.util.Map;
import java.util.HashMap;

class Entity {
    static final String GRAPHICAL_OBJECT = "object-or-widget";
    static final String BEHAVIOR = "behavior";
    static final String BEHAVIOR_EVENT_ID = "behavior-event-id";
    static final String BEHAVIOR_EVENT_KEY = "behavior-event-key";
    static final String BEHAVIOR_EVENT_MODIFIER = "behavior-event-modifier";
    static final String CHANGE_TYPE = "change-type";
    static final String BEHAVIOR_TARGET = "behavior-target";
    static final String GRAPHICS_PARAMETERS = "graphics-parameter";

    static final class GraphicalObjectType {
        static final String FILLED_RECT = "FilledRect";
        static final String OUTLINE_RECT = "OutlineRect";
        static final String FILLED_ELLIPSE = "FilledEllipse";
        static final String OUTLINE_ELLIPSE = "OutlineEllipse";
        static final String TEXT = "Text";
        static final String BUTTON_PANEL = "ButtonPanel";
        static final String CHECKBOX_PANEL = "CheckboxPanel";
        static final String RADIOBUTTON_PANEL = "RadioButtonPanel";
        static final String NUMBER_SLIDER = "NumberSlider";
    }

    static Map<String, Color> stringToColor = new HashMap<>();

    static {
        stringToColor.put("red", Color.RED);
        stringToColor.put("green", Color.GREEN);
        stringToColor.put("blue", Color.BLUE);
        stringToColor.put("yellow", Color.YELLOW);
        stringToColor.put("brown", Color.ORANGE);
    }
}