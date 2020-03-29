package widget;

import graphics.group.Group;
import graphics.group.LayoutGroup;

public class ButtonPanel implements Group, Widget {
    private Group buttons;

    public ButtonPanel(int x, int y, int layout, int offset) {
        this.buttons = new LayoutGroup(x, y, 0, 0, layout, offset);
    }

    public ButtonPanel() {
        this(0, 0, VERTICAL_LAYOUT, 10);
    }
}