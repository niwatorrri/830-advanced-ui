package constraint;

import graphics.object.GraphicalObject;

@FunctionalInterface
public interface SetupConstraint<T extends GraphicalObject> {
    public void setup(T object);
}