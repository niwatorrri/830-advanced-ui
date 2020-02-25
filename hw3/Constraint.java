import java.util.ArrayList;

public abstract class Constraint<T> {

    private T value;
    private ArrayList<GraphicalObject> dependencies = new ArrayList<>();
    // ArrayList<Class<?>> dependencyClasses = new ArrayList<>();

    /**
     * 
     */
    public Constraint() {}

    public Constraint(GraphicalObject... dependencies) {
        // this.value = value;
        for (GraphicalObject dependency: dependencies) {
            this.dependencies.add(dependency);
        }
        // this.setValue(value);
    }

    public ArrayList<GraphicalObject> getDependencies() {
        return new ArrayList<>(this.dependencies);
    }

    /*
     * Evaluates the constraint function if needed and returns the value. May return
     * the value immediately if the constraint doesn't need to be re-evaluated.
     */
    public abstract T getValue();
    // public T getValue() {
    //     return this.value;
    // }

    /*
     * value for the variable this constraint was in was set. This might remove the
     * constraint in a formula constraint system, or set the local value and cause
     * dependency invalidating in a multi-way constraint system
     */
    // public abstract void setValue(T value);
    public void setValue(T value) {
        this.value = value;
    }
}