package behavior;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

import graphics.group.Group;
import graphics.object.GraphicalObject;
import graphics.object.selectable.Selectable;

public class ChoiceBehavior implements Behavior {
    private Group group = null;
    private int state = Behavior.IDLE;

    private int type;
    private boolean firstOnly;
    private List<GraphicalObject> selection = new ArrayList<>();

    public static final int SINGLE = 0;
    public static final int MULTIPLE = 1;

    private BehaviorEvent startEvent = BehaviorEvent.DEFAULT_START_EVENT;
    private BehaviorEvent stopEvent = BehaviorEvent.DEFAULT_STOP_EVENT;
    private BehaviorEvent cancelEvent = BehaviorEvent.DEFAULT_CANCEL_EVENT;

    public ChoiceBehavior(int type, boolean firstOnly) {
        if ((type != SINGLE) && (type != MULTIPLE)) {
            throw new RuntimeException("Unsupported choice behavior type");
        }
        this.type = type;
        this.firstOnly = firstOnly;
    }

    public ChoiceBehavior() {
        this(SINGLE, true);
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

    public List<GraphicalObject> getSelection() {
        return new ArrayList<GraphicalObject>(selection);
    }

    // Convert event coordinates from absolute to relative to group
    private Point findCoordinates(Group group, int x, int y, String option) {
        assert (option == "inside") || (option == "beside");
        if (option == "beside") {
            return group.childToParent(findCoordinates(group, x, y, "inside"));
        } else {
            Group parentGroup = group.getGroup();
            if (parentGroup == null) {
                return new Point(x, y);
            }
            return group.parentToChild(findCoordinates(parentGroup, x, y, option));
        }
    }

    /**
     * start
     */
    public boolean start(BehaviorEvent event) {
        if (event.matches(this.startEvent)
                && this.state == Behavior.IDLE
                && this.group != null) {
            int eventX = event.getX(), eventY = event.getY();
            Point eventInGroup = findCoordinates(group, eventX, eventY, "inside");
            Point eventBesideGroup = group.childToParent(eventInGroup);
            if (!group.contains(eventBesideGroup)) {
                return false;
            }

            // find the object on which the event occurs
            List<GraphicalObject> children = group.getChildren();
            for (int idx = children.size() - 1; idx >= 0; --idx) { // front to back
                GraphicalObject child = children.get(idx);
                if (child.contains(eventInGroup) && child instanceof Selectable) {
                    System.out.println("Choice starts!");
                    this.state = Behavior.RUNNING_INSIDE;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean running(BehaviorEvent event) {
        if (event.matches(this.stopEvent)) {
            return stop(event);
        }
        if (event.matches(this.cancelEvent)) {
            return cancel(event);
        }

        if (this.state != Behavior.IDLE && event.isMouseMoved()) {
            return true;
        }
        return false;
    }

    public boolean stop(BehaviorEvent event) {
        return false;
    }

    public boolean cancel(BehaviorEvent event) {
        return false;
    }

    public boolean check(BehaviorEvent event) {
        return start(event) || running(event) || stop(event) || cancel(event);
    }
}