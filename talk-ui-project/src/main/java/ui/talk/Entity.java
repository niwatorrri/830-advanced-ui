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
    }

    static Map<String, Color> stringToColor = new HashMap<>();

    static {
        stringToColor.put("blue", Color.BLUE);
    }
}