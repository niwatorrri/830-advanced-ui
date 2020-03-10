package constraint;

public class Constraint<T> extends Dependency<T> {
    /**
     * Constraint class: an alias for Dependency class
     */
    public Constraint() {
        super();
    }
    public Constraint(Dependency<?>... dependencies) {
        super(dependencies);
    }
    public Constraint(String name, Dependency<?>... dependencies) {
        super(name, dependencies);
    }
}