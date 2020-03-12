package behavior;

public class BehaviorEvent {
    private int id;
    private int modifiers, key;
    private int x, y;

    public BehaviorEvent(int id, int modifiers, int key, int x, int y) {
        this.id = id;
        this.modifiers = modifiers;
        this.key = key;
        this.x = x;
        this.y = y;
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

    // only supports exact matches. Add support for ANY modifier as extra credit
    public boolean matches(BehaviorEvent event) {
        return 
            (event.id == id)
            && ((modifiers & MAX_MODIFIER) == (event.modifiers & MAX_MODIFIER)) //sometimes there are some out of range
            && (event.key == key);
    }

    public static final int KEY_DOWN_ID = 0;
    public static final int KEY_UP_ID = 1;
    public static final int MOUSE_DOWN_ID = 2;
    public static final int MOUSE_UP_ID = 3;
    public static final int MOUSE_MOVE_ID = 4;
    public static final int SCROLLWHEEL_ID = 5;

    public static final int SHIFT_MODIFIER = 0x1;
    public static final int CONTROL_MODIFIER = 0x2;
    public static final int ALT_MODIFIER = 0x4;
    public static final int WINDOWS_KEY_MODIFIER = 0x8;
    public static final int FUNCTION_KEY_MODIFIER = 0x10;
    public static final int COMMAND_KEY_MODIFIER = 0x20;

    private static final int MAX_MODIFIER = 0x3F; //OR of all supported modifiers; used internally to ignore modifiers that are not supported
   
    public static final int LEFT_MOUSE_KEY = 10000;
    public static final int MIDDLE_MOUSE_KEY = 10001;
    public static final int RIGHT_MOUSE_KEY = 10002;
    public static final int SCROLLWHEEL_UP_KEY = 10003;
    public static final int SCROLLWHEEL_DOWN_KEY = 10004;
}