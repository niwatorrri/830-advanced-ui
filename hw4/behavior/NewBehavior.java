package behavior;

import java.awt.Point;

import graphics.group.Group;
import graphics.object.GraphicalObject;
import constraint.SetupConstraint;

public abstract class NewBehavior implements Behavior {
    /**
     * NewBehavior abstract class: create new objects in the group
     */
    private Group group = null;
    private int state = IDLE;
    private int priority = -1;

    private boolean rectLike;
    private int startX, startY;  // where the behavior starts (wrt group)
    private GraphicalObject newObject = null;
    private SetupConstraint<?> constraint = null;

    private BehaviorEvent startEvent = BehaviorEvent.DEFAULT_START_EVENT;
    private BehaviorEvent stopEvent = BehaviorEvent.DEFAULT_STOP_EVENT;
    private BehaviorEvent cancelEvent = BehaviorEvent.DEFAULT_CANCEL_EVENT;

    public NewBehavior(boolean rectLike, SetupConstraint<?> constraint) {
        this.rectLike = rectLike;
        this.constraint = constraint;
    }

    /**
     * Methods defined in the Behavior interface
     */
    public Group getGroup() {
        return this.group;
    }

    public Behavior setGroup(Group group) {
        this.group = group;
        return this;
    }

    public int getState() {
        return this.state;
    }

    public int getPriority() {
        return this.priority;
    }

    public Behavior setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public BehaviorEvent getStartEvent() {
        return this.startEvent;
    }

    public Behavior setStartEvent(BehaviorEvent startEvent) {
        this.startEvent = startEvent;
        return this;
    }

    public BehaviorEvent getStopEvent() {
        return this.stopEvent;
    }

    public Behavior setStopEvent(BehaviorEvent stopEvent) {
        this.stopEvent = stopEvent;
        return this;
    }

    public BehaviorEvent getCancelEvent() {
        return this.cancelEvent;
    }

    public Behavior setCancelEvent(BehaviorEvent cancelEvent) {
        this.cancelEvent = cancelEvent;
        return this;
    }

    // Compare behavior based on their priorities
    public int compareTo(Behavior behavior) {
        return this.getPriority() - behavior.getPriority();
    }

    // Convert event coordinates from absolute to relative to group
    private Point findCoordinates(Group group, int x, int y) {
        Group parentGroup = group.getGroup();
        if (parentGroup == null) {
            return new Point(x, y);
        }
        return group.parentToChild(findCoordinates(parentGroup, x, y));
    }

    /**
     * Abstract classes to be implemented by subclasses
     */
    public abstract GraphicalObject make(int a, int b, int c, int d, SetupConstraint<?> constraint);
    public abstract void resize(GraphicalObject object, int a, int b, int c, int d);
    public abstract boolean isTrivial(GraphicalObject object);

    /**
     * start
     */
    public boolean start(BehaviorEvent event) {
        if (event.matches(this.startEvent) && this.state == IDLE && this.group != null) {
            int eventX = event.getX(), eventY = event.getY();
            Point eventInGroup = findCoordinates(group, eventX, eventY);
            Point eventBesideGroup = group.childToParent(eventInGroup);
            if (!group.contains(eventBesideGroup)) {
                return false;
            }

            this.startX = eventInGroup.x;
            this.startY = eventInGroup.y;
            if (this.rectLike) {
                newObject = make(startX, startY, 1, 1, constraint);
            } else { // line-like or one-point
                newObject = make(startX, startY, startX, startY, constraint);
            }
            this.group.addChild(newObject);
            this.state = RUNNING_INSIDE;
            return true;
        }
        return false;
    }

    /**
     * running
     */
    public boolean running(BehaviorEvent event) {
        if (event.matches(this.stopEvent) || event.matches(this.cancelEvent)) {
            return false;
        }

        if (this.state != IDLE && event.isMouseMoved()) {
            int eventX = event.getX(), eventY = event.getY();
            Point eventInGroup = findCoordinates(group, eventX, eventY);
            Point eventBesideGroup = group.childToParent(eventInGroup);
            if (!group.contains(eventBesideGroup)) {
                this.state = RUNNING_OUTSIDE;
                return true;
            }

            // resize the newly created object with mouse move
            this.state = RUNNING_INSIDE;
            resize(newObject, startX, startY, eventInGroup.x, eventInGroup.y);
            return true;
        }
        return false;
    }

    /**
     * stop
     */
    public boolean stop(BehaviorEvent event) {
        if (event.matches(this.stopEvent) && this.state != IDLE) {
            if (isTrivial(newObject)) { // zero-sized objects not allowed
                this.group.removeChild(newObject);
            }
            this.state = IDLE;
            return true;
        }
        return false;
    }

    /**
     * cancel
     */
    public boolean cancel(BehaviorEvent event) {
        if (event.matches(this.cancelEvent) && this.state != IDLE) {
            this.group.removeChild(newObject);
            this.state = IDLE;
            return true;
        }
        return false;
    }

    public boolean check(BehaviorEvent event) {
        return start(event) || running(event) || stop(event) || cancel(event);
    }
}