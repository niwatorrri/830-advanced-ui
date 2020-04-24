package ui.talk;

import java.awt.Color;
import java.util.Map;
import java.util.HashMap;

class Entity {
    static final String GRAPHICAL_OBJECT = "object-or-widget";

    static final class GraphicalObjectType {
        static final String FILLED_RECT = "FilledRect";
    }

    static Map<String, Color> stringToColor = new HashMap<>();
    
    static {
        stringToColor.put("blue", Color.BLUE);
    }
}