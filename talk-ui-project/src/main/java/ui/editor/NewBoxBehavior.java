package ui.editor;

import java.awt.Color;

import ui.toolkit.behavior.NewRectBehavior;
import ui.toolkit.constraint.SetupConstraint;
import ui.toolkit.graphics.object.GraphicalObject;

public class NewBoxBehavior extends NewRectBehavior {
    private static int cnt = 0;

    public NewBoxBehavior(int type, Color color, int lineThickness, SetupConstraint constraint) {
        super(type, color, lineThickness, constraint);
    }

    @Override
    public GraphicalObject make(int x, int y, int width, int height, SetupConstraint constraint) {
        Box box = new Box(
            "Box " + ++cnt, x, y, width, height,
            getColor(), getLineThickness(), getType()
        );
        if (constraint != null) {
            constraint.setup(box);
        }
        return box;
    }

    @Override
    public void resize(GraphicalObject object, int x1, int y1, int x2, int y2) {
        super.resize(object, x1, y1, x2, y2);
        super.resize(((Box) object).getFill(), x1, y1, x2, y2);
    }

    @Override
    public boolean isTrivial(GraphicalObject object) {
        boolean trivial = super.isTrivial(object);
        cnt -= (trivial ? 1 : 0);
        return trivial;
    }
}