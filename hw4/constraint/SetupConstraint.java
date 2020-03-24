package constraint;

@FunctionalInterface
public interface SetupConstraint<T> {
    public void setup(T object);
}