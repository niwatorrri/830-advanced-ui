package behavior;

import java.awt.event.KeyEvent;

public class BehaviorEvent {
    /**
     * BehaviorEvent class
     */
    private int modifiers;
    private int key;
    private int id;
    private int x, y;

    public BehaviorEvent(int modifiers, int key, int id, int x, int y) {
        if (modifiers > MODIFIER_MASK) {
            System.err.println("Warning: unsupported modifier detected");
        }
        if (id > MAX_EVENT_ID) {
            System.err.println("Warning: unsupported event ID detected");
        }
        this.modifiers = modifiers;
        this.key = key;
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public BehaviorEvent(int modifiers, int key, int id) {
        this(modifiers, key, id, 0, 0);
    }

    public BehaviorEvent(int modifiers, char key, int id) {
        this(modifiers, Character.toUpperCase(key), id, 0, 0);
    }

    /**
     * Getters
     */
    public int getModifiers() {
        return this.modifiers;
    }

    public int getKey() {
        return this.key;
    }

    public int getID() {
        return this.id;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    /**
     * BehaviorEvent static constants
     */
    // Event modifiers
    public static final int NO_MODIFIER = 0x0;
    public static final int SHIFT_MODIFIER = 0x1;
    public static final int CONTROL_MODIFIER = 0x2;
    public static final int ALT_MODIFIER = 0x4;
    public static final int COMMAND_MODIFIER = 0x8;
    public static final int WINDOWS_KEY_MODIFIER = 0x10;    // unsupported
    public static final int FUNCTION_KEY_MODIFIER = 0x20;   // unsupported
    public static final int ANY_MODIFIER = -1;
    private static final int MODIFIER_MASK = 0xf;   // mask for supported modifiers only

    // Event keys (mouse keys)
    public static final int NO_KEY = 0;
    public static final int LEFT_MOUSE_KEY = 10000;
    public static final int MIDDLE_MOUSE_KEY = 10001;
    public static final int RIGHT_MOUSE_KEY = 10002;
    public static final int SCROLLWHEEL_UP_KEY = 10003;
    public static final int SCROLLWHEEL_DOWN_KEY = 10004;

    // Event IDs
    public static final int KEY_DOWN_ID = 0;
    public static final int KEY_UP_ID = 1;
    public static final int MOUSE_DOWN_ID = 2;
    public static final int MOUSE_UP_ID = 3;
    public static final int MOUSE_MOVE_ID = 4;
    public static final int MOUSE_DRAG_ID = 5;
    public static final int MOUSE_CLICK_ID = 6;
    public static final int SCROLLWHEEL_ID = 7;
    private static final int MAX_EVENT_ID = 7;

    // Default behavior events
    public static final BehaviorEvent LEFT_MOUSE_DOWN = new BehaviorEvent(
        NO_MODIFIER, LEFT_MOUSE_KEY, MOUSE_DOWN_ID);
    public static final BehaviorEvent LEFT_MOUSE_UP = new BehaviorEvent(
        ANY_MODIFIER, LEFT_MOUSE_KEY, MOUSE_UP_ID);
    public static final BehaviorEvent ESCAPE_KEY_UP = new BehaviorEvent(
        NO_MODIFIER, KeyEvent.VK_ESCAPE, KEY_UP_ID);
    public static final BehaviorEvent DEFAULT_START_EVENT = LEFT_MOUSE_DOWN;
    public static final BehaviorEvent DEFAULT_STOP_EVENT = LEFT_MOUSE_UP;
    public static final BehaviorEvent DEFAULT_CANCEL_EVENT = ESCAPE_KEY_UP;

    /**
     * Utilities
     */
    public boolean matches(BehaviorEvent event) {
        boolean modifiersMatched = false;
        if (event.modifiers == ANY_MODIFIER || this.modifiers == ANY_MODIFIER) {
            modifiersMatched = true;
        } else {
            modifiersMatched = (event.modifiers & MODIFIER_MASK) == (this.modifiers & MODIFIER_MASK);
        }
        return modifiersMatched && (event.id == this.id) && (event.key == this.key);
    }

    public boolean isMouseMoved() {
        return (id == MOUSE_MOVE_ID) || (id == MOUSE_DRAG_ID);
    }
    
    public static boolean isMouseEvent(int id) {
        return (id == MOUSE_DOWN_ID) || (id == MOUSE_UP_ID) || (id == MOUSE_MOVE_ID)
                || (id == MOUSE_DRAG_ID) || (id == MOUSE_CLICK_ID);
    }

    public static boolean isMouseWheelEvent(int id) {
        return (id == SCROLLWHEEL_ID);
    }

    public static boolean isKeyEvent(int id) {
        return (id == KEY_DOWN_ID) || (id == KEY_UP_ID);
    }
}