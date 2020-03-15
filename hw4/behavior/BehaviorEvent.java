package behavior;

import java.awt.event.KeyEvent;

public class BehaviorEvent {
    private int id;
    private int modifiers;
    private int key;
    private int x, y;

    public BehaviorEvent(int id, int modifiers, int key, int x, int y) {
        this.id = id;
        this.modifiers = modifiers;
        this.key = key;
        this.x = x;
        this.y = y;
    }

    public BehaviorEvent(int id, int modifiers, int key) {
        this(id, modifiers, key, 0, 0);
    }

    public int getID() {
        return this.id;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public int getKey() {
        return this.key;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    // Behavior event static constants
    public static final int KEY_DOWN_ID = 0;
    public static final int KEY_UP_ID = 1;
    public static final int MOUSE_DOWN_ID = 2;
    public static final int MOUSE_UP_ID = 3;
    public static final int MOUSE_MOVE_ID = 4;
    public static final int MOUSE_DRAGGED_ID = 5;
    public static final int SCROLLWHEEL_ID = 6;

    public static final int NO_MODIFIER = 0x0;
    public static final int SHIFT_MODIFIER = 0x1;
    public static final int CONTROL_MODIFIER = 0x2;
    public static final int ALT_MODIFIER = 0x4;
    public static final int WINDOWS_KEY_MODIFIER = 0x8;
    public static final int FUNCTION_KEY_MODIFIER = 0x10;
    public static final int COMMAND_KEY_MODIFIER = 0x20;

    private static final int MAX_MODIFIER = 0x3F; // OR of all supported modifiers

    public static final int NO_KEY = 0;
    public static final int LEFT_MOUSE_KEY = 10000;
    public static final int MIDDLE_MOUSE_KEY = 10001;
    public static final int RIGHT_MOUSE_KEY = 10002;
    public static final int SCROLLWHEEL_UP_KEY = 10003;
    public static final int SCROLLWHEEL_DOWN_KEY = 10004;

    public static BehaviorEvent DEFAULT_START_EVENT = new BehaviorEvent(
        MOUSE_DOWN_ID, NO_MODIFIER, LEFT_MOUSE_KEY);
    public static BehaviorEvent DEFAULT_STOP_EVENT = new BehaviorEvent(
        MOUSE_UP_ID, NO_MODIFIER, LEFT_MOUSE_KEY);
    public static BehaviorEvent DEFAULT_CANCEL_EVENT = new BehaviorEvent(
        KEY_UP_ID, NO_MODIFIER, KeyEvent.VK_ESCAPE);

    // only supports exact matches. Add support for ANY modifier as extra credit
    public boolean matches(BehaviorEvent event) {
        return (event.id == this.id)
                && ((event.modifiers & MAX_MODIFIER) == (this.modifiers & MAX_MODIFIER))
                && (event.key == this.key);
    }

    public static boolean isMouseEvent(int id) {
        return (id == MOUSE_DOWN_ID) || (id == MOUSE_UP_ID)
                || (id == MOUSE_MOVE_ID) || (id == MOUSE_DRAGGED_ID);
    }

    public static boolean isMouseWheelEvent(int id) {
        return (id == SCROLLWHEEL_ID);
    }

    public static boolean isKeyEvent(int id) {
        return (id == KEY_DOWN_ID) || (id == KEY_UP_ID);
    }
}