package behavior;

import graphics.object.GraphicalObject;

public abstract class NewBehavior implements Behavior {
    public NewBehavior(boolean onePoint);
    public abstract GraphicalObject make(int x1, int y1, int x2, int y2);
    public abstract void resize(GraphicalObject gobj, int x1, int y1, int x2, int y2);
}