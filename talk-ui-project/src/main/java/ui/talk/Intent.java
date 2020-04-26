package ui.talk;

class Intent {
    static final String INITIALIZATION = "talkui.initialization";

    static final class InitializationParams {
        static final String GRAPHICAL_OBJECT = "object-or-widget";
        static final String WIDTH = "width";
        static final String HEIGHT = "height";
        static final String COLOR = "color";
    }
}