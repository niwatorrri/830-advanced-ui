import java.util.List;
import java.util.ArrayList;

public class Constraint<T> extends Dependency<T> {
    public Constraint(Dependency<?>... dependencies) {
        super(dependencies);
    }
}

class Dependency<T> {
    private T value;
    private boolean outOfDate = false;
    private List<Edge> outEdges = new ArrayList<>();
    private List<Edge> inEdges = new ArrayList<>();

    public Dependency(Dependency<?>... dependencies) {
        for (Dependency<?> dependency: dependencies) {
            this.inEdges.add(new Edge(dependency));
            dependency.outEdges.add(new Edge(this));
        }
    }

    public T getValue() {
        return this.value;
    }

    public List<Edge> getOutEdges() {
        return this.outEdges;
    }

    public List<Edge> getInEdges() {
        return this.inEdges;
    }

    public boolean isConstrained() {
        return (inEdges.size() != 0);
    }

    public void setOutOfDate(boolean outOfDate) {
        this.outOfDate = outOfDate;
    }

    public void markOutOfDate() {
        if (!this.outOfDate) {
            this.outOfDate = true;
            for (Edge outEdge : this.outEdges) {
                // outEdge.setPending(true);
                outEdge.markOutOfDate();
            }
        }
    }

    public T evaluate() {
        // TODO: detect cycles and support multiway constraints
        if (this.outOfDate) {
            this.outOfDate = false;

            boolean needReevaluate = false;
            for (Edge inEdge: inEdges) {
                needReevaluate = needReevaluate || inEdge.isPending();
            }
            if (needReevaluate) {
                T newValue = this.getValue();
                if (newValue != this.value) {
                    this.value = newValue;
                    for (Edge outEdge: outEdges) {
                        outEdge.setPending(true);
                    }
                }
            }
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

    public boolean isPending() {
        return this.isPending;
    }

    public void setPending(boolean isPending) {
        this.isPending = isPending;
    }

    public void markOutOfDate() {
        this.object.markOutOfDate();
    }
}