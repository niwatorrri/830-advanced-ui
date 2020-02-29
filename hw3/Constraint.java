import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class Constraint<T> extends Dependency<T> {
    public Constraint() {
        super();
    }
    public Constraint(Dependency<?>... dependencies) {
        super(dependencies);
    }
}

class Dependency<T> {
    private T value;
    private boolean outOfDate = false;
    private List<Edge> outEdges = new ArrayList<>();
    private List<Edge> inEdges = new ArrayList<>();

    public Dependency() {}

    public Dependency(Dependency<?>... dependencies) {
        // set up incoming and outgoing edges in the dependency graph
        for (Dependency<?> dependency: dependencies) {
            this.inEdges.add(new Edge(dependency));
            dependency.outEdges.add(new Edge(this));
        }
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public List<Edge> getOutEdges() {
        return this.outEdges;
    }

    public List<Edge> getInEdges() {
        return this.inEdges;
    }

    public void setOutOfDate(boolean outOfDate) {
        this.outOfDate = outOfDate;
    }

    public boolean isConstrained() {
        return (inEdges.size() != 0);
    }

    public void addInEdge(Dependency<?> constraint) {
        this.inEdges.add(new Edge(constraint));
    }

    public void addOutEdge(Dependency<?> constraint) {
        this.outEdges.add(new Edge(constraint));
    }

    public void updateConstraint(Dependency<T> newConstraint) {
        // remove previous outgoing changes
        for (Edge outEdge: this.outEdges) {
            Dependency<?> target = outEdge.getObject();
            Iterator<Edge> iter = target.getInEdges().iterator();
            while (iter.hasNext()) {
                if (iter.next().getObject() == this) {
                    iter.remove();
                }
            }
            // add new outgoing edges
            target.addInEdge(newConstraint);
            newConstraint.addOutEdge(target);
        }

        // remove previous incoming edges
        for (Edge inEdge: this.inEdges) {
            Dependency<?> dependency = inEdge.getObject();
            Iterator<Edge> iter = dependency.getOutEdges().iterator();
            while (iter.hasNext()) {
                if (iter.next().getObject() == this) {
                    iter.remove();
                }
            }
        }
    }

    public void markOutOfDate() {
        if (!this.outOfDate) {
            this.outOfDate = true;
            for (Edge outEdge : this.outEdges) {
                outEdge.markOutOfDate();
            }
        }
    }

    public T evaluate() {
        // TODO: detect cycles
        // deal with constraint conflicts / support multiway constraints

        // consider re-evaluating if out of date
        if (this.outOfDate) {
            // check if there are any pending incoming edges
            boolean anyPending = false;
            for (Edge inEdge: inEdges) {
                anyPending = anyPending || inEdge.isPending();
                inEdge.setPending(false);
            }

            // re-evaluate the constraint
            if (anyPending) {
                try {
                    T newValue = this.getValue();
                    if (newValue != this.value) {
                        // if value changes, set outgoing edges to be pending
                        this.value = newValue;
                        for (Edge outEdge : outEdges) {
                            outEdge.setPending(true);
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e);
                    return this.value;
                }
            }
            // update outOfDate as the final step
            // in case user implemented getValue() crashes
            this.outOfDate = false;
        }
        return this.value;
    }
}

class Edge {
    private Dependency<?> object;
    private boolean isPending;

    public Edge(Dependency<?> object) {
        this.object = object;
        this.isPending = true;
    }

    public Dependency<?> getObject() {
        return this.object;
    }

    public boolean isPending() {
        return this.isPending;
    }

    public void setPending(boolean isPending) {
        this.isPending = isPending;
    }

    public void markOutOfDate() {
        this.object.markOutOfDate();
    }

    // public String toString() {
    //     return "[" + object.toString() + ", isPending=" + (String)isPending + "]";
    // }
}