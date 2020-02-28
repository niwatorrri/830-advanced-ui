import java.util.List;
import java.util.ArrayList;

/*
abstract class Constraint<T> {
    private Dependency<T> target = null;
    private List<Dependency<?>> dependencies = new ArrayList<>();

    public Constraint() {}

    public Constraint(Object... inputs) {
        // this.value = value;
        GraphicalObject object = null;
        String attribute = null;

        for (Object input: inputs) {
            if (input instanceof GraphicalObject) {
                object = (GraphicalObject) input;
            } else if (input instanceof String) {
                attribute = (String) input;
                if (this.target != null) {
                    this.dependencies.add(new Dependency(object, attribute, target));
                } else { // TODO: error handling
                    this.target = new Dependency(object, attribute);
                    continue;
                }
            } else {
                // error
            }
        }
        // this.setValue(value);
    }
    // Evaluates the constraint function if needed and returns the value. May return
    // the value immediately if the constraint doesn't need to be re-evaluated.
    public abstract T getValue();
    // public T getValue() {
    //     return this.value;
    // }

    public T evaluate() {
        if (target.isOutOfDate()) {
            target.setOutOfDate(false);

        }
        return this.getValue();
    }

    // value for the variable this constraint was in was set. This might remove the
    // constraint in a formula constraint system, or set the local value and cause
    // dependency invalidating in a multi-way constraint system

    // public abstract void setValue(T value);
    public void setValue(T value) {
        this.value = value;
    }

    public Dependency findDependency(GraphicalObject object, String attribute) {
        for (Dependency dependency: this.dependencies) {
            if (dependency.hasObjectAndAttribute(object, attribute)) {
                return dependency;
            }
        }
        return null;
    }
}
*/

public class Dependency<T> {
    public T value;
    private boolean outOfDate = false;
    public List<Edge> outEdges = new ArrayList<>();
    public List<Edge> inEdges = new ArrayList<>();

    public Dependency(T initialValue) {
        this.value = initialValue;
        System.out.println(this.value);
    }

    public Dependency(Dependency<?>... dependencies) {
        for (Dependency<?> dependency: dependencies) {
            this.inEdges.add(new Edge(dependency));
            dependency.outEdges.add(new Edge(this));
        }
    }

    public T getValue() {
        return this.value;
    }

    public boolean isConstrained() {
        return (inEdges.size() != 0);
    }
    // public T getValue() {
    // return this.value;
    // }

    public T evaluate() {
        System.out.println("in evaluate");
        System.out.println(outOfDate);
        if (this.outOfDate) {
            this.outOfDate = false;

            System.out.println(this.value);
            boolean needReevaluate = false;
            for (Edge inEdge: inEdges) {
                needReevaluate = needReevaluate || inEdge.isPending();
            }
            System.out.println("Needs reevaluating? " + (needReevaluate ? "T" :"F"));
            if (needReevaluate) {
                T newValue = this.getValue();
                System.out.println("New value: " + (newValue.toString()));
                if (newValue != this.value) {
                    this.value = newValue;
                    for (Edge outEdge: outEdges) {
                        System.out.println("one pending");
                        outEdge.setPending(true);
                    }
                }
            }
        }
        return this.value;
    }

    public boolean isOutOfDate() {
        return this.outOfDate;
    }

    public void markOutOfDate() {
        if (!this.outOfDate) {
            this.outOfDate = true;
            for (Edge outEdge: this.outEdges) {
                outEdge.setPending(true);
                outEdge.object.markOutOfDate();
            }
        }
    }
}

class Edge {
    public Dependency<?> object;
    private boolean isPending;

    public Edge(Dependency<?> object) {
        this.object = object;
        this.isPending = true;
    }

    public boolean isPending() {
        return this.isPending;
    }

    public void setPending(boolean isPending) {
        this.isPending = isPending;
    }
}

/*
class Dependency<T> {
    private GraphicalObject object;
    private String attribute;
    private T value;

    private Dependency<?> target;
    private boolean outOfDate;

    public Dependency(GraphicalObject object, String attribute) {
        this(object, attribute, null);
    }

    public Dependency(GraphicalObject object, String attribute, Dependency<?> target) {
        this.object = object;
        this.attribute = attribute;
        this.target = target;
        this.outOfDate = false;
    }

    public void markOutOfDate() {
        if (!this.outOfDate) {
            this.outOfDate = true;
            this.target.markOutOfDate();
        }
    }

    // public GraphicalObject getObject() {
    //     return this.object;
    // }

    // public String getAttribute() {
    //     return this.attribute;
    // }
    public boolean hasObjectAndAttribute(GraphicalObject object, String attribute) {
        return this.object == object && this.attribute == attribute;
    }

    public T evaluate() {
        if (this.outOfDate) {
            this.outOfDate = false;

        }
        return this.value;
    }
}
*/