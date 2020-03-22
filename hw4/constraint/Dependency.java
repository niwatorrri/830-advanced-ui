package constraint;

import java.util.List;
import java.util.ArrayList;

public class Dependency<T> {
    /**
     * Dependency class: vertices in the dependency graph
     */
    private T value;
    private boolean outOfDate = true;
    private boolean visited = false;
    private boolean evaluated = true;
    private List<Edge> outEdges = new ArrayList<>();
    private List<Edge> inEdges = new ArrayList<>();
    private String name = null;

    public Dependency() {}

    public Dependency(Dependency<?>... dependencies) {
        // set up incoming and outgoing edges in the dependency graph
        for (Dependency<?> dependency: dependencies) {
            Edge edge = new Edge(dependency, this);
            this.inEdges.add(edge);
            dependency.outEdges.add(edge);
        }
    }

    public Dependency(String name, Dependency<?>... dependencies) {
        this(dependencies);
        this.name = name;
    }

    /**
     * This should be overridden by custom constraints
     */
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
        return (this.inEdges.size() > 0);
    }

    public String toString() {
        return (this.name != null) ? this.name : super.toString();
    }

    /**
     * Replace this old constraint with a new constraint
     * 
     * @param newConstraint
     */
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
    
    /**
     * Detect if the constraint is involved in a dependency cycle
     */
    public boolean hasCycle() {
        if (this.visited) {
            return true;
        }

        this.visited = true;
        boolean cycleDetected = false;
        for (Edge outEdge : this.outEdges) {
            if (outEdge.getEnd().hasCycle()) {
                cycleDetected = true;
                break;
            }
        }
        this.visited = false;
        return cycleDetected;
    }

    /**
     * Notify its direct successor to propagate out-of-date signals
     * 
     * @param selfOutOfDate whether to mark itself as out-of-date
     */
    public void notifyValueChange(boolean selfOutOfDate) {
        this.visited = true;
        this.outOfDate = selfOutOfDate;
        for (Edge outEdge : this.outEdges) {
            outEdge.setPending(true);
            outEdge.getEnd().markOutOfDate();
        }
        this.visited = false;
    }    

    /**
     * Mark all successors of this dependency as out-of-date
     */
    public void markOutOfDate() {
        if (this.visited) {
            return;
        }

        this.visited = true;
        if (!this.outOfDate) {
            this.outOfDate = true;
            for (Edge outEdge : this.outEdges) {
                outEdge.getEnd().markOutOfDate();
            }
        }
        this.visited = false;
    }

    /**
     * Lazy evaluation of constraint (based on Hudson's algorithm)
     * 
     * @return evaluated constraint value
     */
    public T evaluate() {
        // yet to be evaluated
        this.evaluated = false;

        // consider re-evaluating if out of date
        if (this.outOfDate) {
            // check if there are any pending incoming edges
            boolean anyPending = false;
            for (Edge inEdge: this.inEdges) {
                anyPending = anyPending || inEdge.isPending();
                inEdge.setPending(false);
            }

            // re-evaluate the constraint
            if (anyPending) {
                try {
                    T newValue = this.getValue();
                    if (newValue != this.value) {
                        if (this.evaluated) {
                            // if cycle, report cyclic dependency conflict
                            System.err.println(
                                "ERROR: Conflict found when evaluating constraint " + this
                            );
                        } else {
                            // if no cycles, update value and set pending edges
                            this.value = newValue;
                            for (Edge outEdge : this.outEdges) {
                                outEdge.setPending(true);
                            }
                        }
                    }
                } catch (Exception e) {
                    // error in user-implemented getValue method
                    System.err.println(String.format(
                        "ERROR: in %s.getValue(): %s", this, e
                    ));
                    return this.value;
                }
            }
            // update outOfDate as the final step
            // in case user implemented getValue() crashes
            this.outOfDate = false;
        }
        // finish evaluation
        this.evaluated = true;
        return this.value;
    }
}

class Edge {
    /**
     * Edge class: directed edges in the dependency graph
     */
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

    private String outOfDateToString(boolean outOfDate) {
        return outOfDate ? "out of date" : "up to date";
    }

    public String toString() {
        return String.format("%s (%s) -> %s (%s): %s", 
            start.toString(), outOfDateToString(start.isOutOfDate()),
            end.toString(), outOfDateToString(end.isOutOfDate()),
            isPending ? "pending" : "up to date"
        );
    }
}