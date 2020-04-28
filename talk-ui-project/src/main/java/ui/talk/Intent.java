package ui.talk;

class Intent {
    static final String INITIALIZATION = "talkui.initialization";
    static final String INTERACTION = "talkui.interaction";

    static final class InitializationParams {
        static final String GRAPHICAL_OBJECT = "object-or-widget";
        static final String WIDTH = "width";
        static final String HEIGHT = "height";
        static final String COLOR = "color";
        static final String THICKNESS = "thickness";
        static final String TEXT = "text";
        static final String OPTIONS = "options";
        static final String COUNT = "count";
    }

    static final class InteractionParams {
        static final String GRAPHICS_PARAMETER = "graphics-parameter";
        static final String NUMBER_INTEGER = "number-integer";
        static final String COLOR = "color";
        static final String BEHAVIOR_EVENT_KEY = "behavior-event-key";
        static final String BEHAVIOR_EVENT_MODIFIER = "behavior-event-modifier";
        static final String CHANGE_TYPE = "change-type";
        static final String BEHAVIOR_EVENT_ID = "behavior-event-id";
        static final String PERCENTAGE = "percentage";
        static final String BEHAVIOR_TARGET = "behavior-target";
        static final String BEHAVIOR = "behavior";
    }
}