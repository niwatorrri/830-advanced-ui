package ui.toolkit.behavior;

import java.util.List;

import ui.toolkit.graphics.group.SimpleGroup;

public class TopGroup extends SimpleGroup {
    private boolean behaviorsSorted = false;

    public TopGroup(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public boolean isBehaviorsSorted() {
        return behaviorsSorted;
    }

    public TopGroup setBehaviorsSorted(boolean behaviorsSorted) {
        this.behaviorsSorted = behaviorsSorted;
        return this;
    }

    @Override
    public List<Behavior> getBehaviors() {
        return behaviors;
    }
}