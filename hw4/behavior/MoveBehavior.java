package behavior;

import java.awt.Point;
import java.util.List;

import graphics.group.Group;
import graphics.object.BoundaryRectangle;
import graphics.object.GraphicalObject;

public class MoveBehavior implements Behavior {
    private Group group = null;
    private int state = Behavior.IDLE;

    private int startX, startY;  // location of start event
    private int prevX, prevY;    // location of previous move
    private GraphicalObject movingObject;

    private BehaviorEvent startEvent = BehaviorEvent.DEFAULT_START_EVENT;
    private BehaviorEvent stopEvent = BehaviorEvent.DEFAULT_STOP_EVENT;
    private BehaviorEvent cancelEvent = BehaviorEvent.DEFAULT_CANCEL_EVENT;

    public MoveBehavior() {
        // nothing needs to be done
    }

    /**
     * Methods defined in the Behavior interface
     */
    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
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

    public BehaviorEvent getCancelEvent() {
        return this.cancelEvent;
    }

    public void setCancelEvent(BehaviorEvent cancelEvent) {
        this.cancelEvent = cancelEvent;
    }

    private Point findCoordinates(Group group, int x, int y) {
        if (group.getGroup() == null) {
            return new Point(x, y);
        }
        return group.parentToChild(findCoordinates(group.getGroup(), x, y));
    }

    /**
     * start
     */
    public boolean start(BehaviorEvent event) {
        if (event.matches(this.startEvent)
                && this.state == Behavior.IDLE
                && this.group != null) {
            int eventX = event.getX(), eventY = event.getY();
            Point eventInGroup = findCoordinates(group, eventX, eventY);

            // find the object to be moved
            List<GraphicalObject> children = group.getChildren();
            for (int idx = children.size() - 1; idx >= 0; --idx) { // front to back
                GraphicalObject child = children.get(idx);
                if (child.contains(eventInGroup.x, eventInGroup.y)) {
                    System.out.println("Move starts!");
                    this.startX = this.prevX = eventX;
                    this.startY = this.prevY = eventY;
                    this.movingObject = child;
                    this.state = Behavior.RUNNING_INSIDE;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * running
     */
    public boolean running(BehaviorEvent event) {
        if (event.matches(this.stopEvent)) {
            return stop(event);
        }
        if (event.matches(this.cancelEvent)) {
            return cancel(event);
        }

        // TODO: need fixes on MOUSE_DRAGGED_ID as the start event may be not left mouse click
        if (this.state != Behavior.IDLE
                && event.getID() == BehaviorEvent.MOUSE_DRAGGED_ID) {
            int eventX = event.getX(), eventY = event.getY();
            Point eventBesideGroup = group.childToParent(findCoordinates(group, eventX, eventY));
            if (!group.getBoundingBox().contains(eventBesideGroup.x, eventBesideGroup.y)) {
                System.out.println("outside!");
                this.state = Behavior.RUNNING_OUTSIDE;
                return true;
            }

            // move the object with mouse
            this.state = Behavior.RUNNING_INSIDE;
            BoundaryRectangle r = movingObject.getBoundingBox();
            int newX = r.x - prevX + eventX;
            int newY = r.y - prevY + eventY;
            movingObject.moveTo(newX, newY);
            prevX = eventX;
            prevY = eventY;
            return true;
        }
        return false;
    }

    /**
     * stop
     */
    public boolean stop(BehaviorEvent event) {
        if (event.matches(this.stopEvent)) {
            this.state = Behavior.IDLE;
            return true;
        }
        return false;
    }

    /**
     * cancel
     */
    public boolean cancel(BehaviorEvent event) {
        if (event.matches(this.cancelEvent)
                && this.state != Behavior.IDLE) {
            BoundaryRectangle r = movingObject.getBoundingBox();
            int originalX = r.x - prevX + startX;
            int originalY = r.y - prevY + startY;
            movingObject.moveTo(originalX, originalY);
            this.state = Behavior.IDLE;
            return true;
        }
        return false;
    }

    public boolean check(BehaviorEvent event) {
        return start(event) || running(event) || stop(event) || cancel(event);
    }
}