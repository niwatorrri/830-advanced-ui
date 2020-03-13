package behavior;

import graphics.group.Group;
import graphics.object.GraphicalObject;

public class MoveBehavior implements Behavior {
    private Group group;
    private int state;

    private BehaviorEvent startEvent;
    private BehaviorEvent stopEvent;

    public MoveBehavior() {
        // TODO
        this.group = null;
        this.state = Behavior.IDLE;
        this.startEvent = new BehaviorEvent(
            BehaviorEvent.MOUSE_DOWN_ID,
            BehaviorEvent.NO_MODIFIER,
            BehaviorEvent.LEFT_MOUSE_KEY, 0, 0
        );
        this.stopEvent = new BehaviorEvent(
            BehaviorEvent.MOUSE_UP_ID,
            BehaviorEvent.NO_MODIFIER,
            BehaviorEvent.LEFT_MOUSE_KEY, 0, 0
        );
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
        // TODO: set behavior in group?
    }

    public int getState() {
        return this.state;
    }

    public BehaviorEvent getStartEvent() {
        return this.startEvent;
    }

    public void setStartEvent(BehaviorEvent startEvent) {
        this.startEvent = startEvent;
    }

    public BehaviorEvent getStopEvent() {
        return this.stopEvent;
    }

    public void setStopEvent(BehaviorEvent stopEvent) {
        this.stopEvent = stopEvent;
    }

    public Boolean start(BehaviorEvent event) {
        if (event.matches(this.startEvent)) {
            for (GraphicalObject child : group.getChildren()) {
                if (child.contains(x, y)) {
                    this.state = Behavior.RUNNING_INSIDE;
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean running(BehaviorEvent event);

    public Boolean stop(BehaviorEvent event);

    public Boolean cancel(BehaviorEvent event);
}