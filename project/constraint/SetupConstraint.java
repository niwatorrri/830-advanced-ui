package constraint;

import graphics.object.GraphicalObject;

@FunctionalInterface
public interface SetupConstraint {
    public void setup(GraphicalObject... objects);
}