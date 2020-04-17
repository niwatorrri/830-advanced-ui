package ui.toolkit.constraint;

import ui.toolkit.graphics.object.GraphicalObject;

@FunctionalInterface
public interface SetupConstraint {
    public void setup(GraphicalObject... objects);
}