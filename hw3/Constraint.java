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
    private boolean outOfDate = true;
    private List<Edge> outEdges = new ArrayList<>();
    private List<Edge> inEdges = new ArrayList<>();

    public Dependency() {}

    public Dependency(Dependency<?>... dependencies) {
        // set up incoming and outgoing edges in the dependency graph
        for (Dependency<?> dependency: dependencies) {
            Edge edge = new Edge(dependency, this);
            this.inEdges.add(edge);
            dependency.outEdges.add(edge);
        }
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isOutOfDate() {
        return this.outOfDate;
    }

    public void setOutOfDate(boolean outOfDate) {
        this.outOfDate = outOfDate;
    }

    public List<Edge> getOutEdges() {
        return this.outEdges;
    }

    public void addOutEdge(Edge edge) {
        this.outEdges.add(edge);
    }

    public void removeOutEdge(Edge edge) {
        this.outEdges.remove(edge);
    }

    public List<Edge> getInEdges() {
        return this.inEdges;
    }

    public void addInEdge(Edge edge) {
        this.inEdges.add(edge);
    }

    public void removeInEdge(Edge edge) {
        this.inEdges.remove(edge);
    }

    public boolean isConstrained() {
        return (inEdges.size() != 0);
    }

    public void updateConstraint(Dependency<T> newConstraint) {
        // remove previous outgoing edges
        for (Edge outEdge: this.outEdges) {
            Dependency<?> target = outEdge.getEnd();
            target.removeInEdge(outEdge);

            // add new outgoing edges
            Edge edge = new Edge(newConstraint, target);
            target.addInEdge(edge);
            newConstraint.addOutEdge(edge);
        }

        // remove previous incoming edges
        for (Edge inEdge: this.inEdges) {
            inEdge.getStart().removeOutEdge(inEdge);
        }
    }

    public void markOutOfDate() {
        if (!this.outOfDate) {
            this.outOfDate = true;
            for (Edge outEdge : this.outEdges) {
                outEdge.getEnd().markOutOfDate();
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
    private Dependency<?> start;
    private Dependency<?> end;
    private boolean isPending;

    public Edge(Dependency<?> start, Dependency<?> end) {
        this.start = start;
        this.end = end;
        this.isPending = true;
    }

    public Dependency<?> getStart() {
        return this.start;
    }

    public Dependency<?> getEnd() {
        return this.end;
    }

    public boolean isPending() {
        return this.isPending;
    }

    public void setPending(boolean isPending) {
        this.isPending = isPending;
    }

    // public void markOutOfDate() {
    //     this.object.markOutOfDate();
    // }

    // public String toString() {
    //     return "[" + object.toString() + ", isPending=" + (String)isPending + "]";
    // }
}