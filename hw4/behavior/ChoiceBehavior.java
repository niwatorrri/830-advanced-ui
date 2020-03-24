package behavior;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

import graphics.group.Group;
import graphics.object.GraphicalObject;
import graphics.object.selectable.SelectableGraphicalObject;

public class ChoiceBehavior implements Behavior {
    /**
     * ChoiceBehavior: select one or more objects in the group
     */
    private Group group = null;
    private int state = IDLE;

    private int type;           // control selected in stop
    private boolean firstOnly;  // control interimSelected in running
    private boolean startInGroup;
    private SelectableGraphicalObject firstObject;
    private List<GraphicalObject> selection = new ArrayList<>();

    // Static constants for selection type
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

    public Behavior setGroup(Group group) {
        this.group = group;
        return this;
    }

    public int getState() {
        return this.state;
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

    public List<GraphicalObject> getSelection() {
        return new ArrayList<GraphicalObject>(selection);
    }

    // Convert event coordinates from absolute to relative to group
    private Point findCoordinates(Group group, int x, int y) {
        Group parentGroup = group.getGroup();
        if (parentGroup == null) {
            return new Point(x, y);
        }
        return group.parentToChild(findCoordinates(parentGroup, x, y));
    }

    // De-select all the selected objects
    private void clearSelection() {
        for (GraphicalObject selectedObject : selection) {
            ((SelectableGraphicalObject) selectedObject).setSelected(false);
        }
        selection.clear();
    }

    /**
     * start
     */
    public boolean start(BehaviorEvent event) {
        if (event.matches(this.startEvent) && this.state == IDLE && this.group != null) {
            // check if event occurs within the group
            int eventX = event.getX(), eventY = event.getY();
            Point eventInGroup = findCoordinates(group, eventX, eventY);
            Point eventBesideGroup = group.childToParent(eventInGroup);
            if (!group.contains(eventBesideGroup)) {
                return false;
            }

            // find the object on which the event occurs
            this.startInGroup = true;
            List<GraphicalObject> children = group.getChildren();
            for (int idx = children.size() - 1; idx >= 0; --idx) { // front to back
                GraphicalObject child = children.get(idx);
                if (child.contains(eventInGroup) && child instanceof SelectableGraphicalObject) {
                    SelectableGraphicalObject selectableChild = (SelectableGraphicalObject) child;
                    selectableChild.setInterimSelected(true);
                    this.firstObject = selectableChild;
                    this.state = RUNNING_INSIDE;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * running - update interimSelected
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
            this.state = RUNNING_INSIDE;

            // case 1: only the initial object can be toggled
            if (this.firstOnly) {
                firstObject.setInterimSelected(firstObject.contains(eventInGroup));
                return true;
            }

            // case 2: target object can change as mouse moves
            List<GraphicalObject> children = group.getChildren();
            boolean interimSelectionOccupied = false;
            for (int idx = children.size() - 1; idx >= 0; --idx) { // front to back
                GraphicalObject child = children.get(idx);
                if (child instanceof SelectableGraphicalObject) {
                    SelectableGraphicalObject selectableChild = (SelectableGraphicalObject) child;
                    if (!interimSelectionOccupied && selectableChild.contains(eventInGroup)) {
                        selectableChild.setInterimSelected(true);
                        interimSelectionOccupied = true;
                    } else {
                        selectableChild.setInterimSelected(false);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * stop - update selected
     */
    public boolean stop(BehaviorEvent event) {
        if (event.matches(this.stopEvent)) {
            int eventX = event.getX(), eventY = event.getY();
            Point eventInGroup = findCoordinates(group, eventX, eventY);
            Point eventBesideGroup = group.childToParent(eventInGroup);
            if (!group.contains(eventBesideGroup)) {
                return false;
            }

            // case 1: start outside the group
            if (!this.startInGroup) {
                return true;
            }
            this.startInGroup = false;

            // case 2: start inside the group but not on any object
            if (this.state == IDLE) {
                if (this.type == SINGLE) {
                    clearSelection();
                }
                return true;
            }

            // case 3: start inside the group and on some object
            SelectableGraphicalObject targetObject = null;
            if (this.firstOnly) {
                targetObject = firstObject;
            } else {
                List<GraphicalObject> children = group.getChildren();
                for (int idx = children.size() - 1; idx >= 0; --idx) { // front to back
                    GraphicalObject child = children.get(idx);
                    if (child instanceof SelectableGraphicalObject) {
                        SelectableGraphicalObject selectableChild = (SelectableGraphicalObject) child;
                        if (selectableChild.contains(eventInGroup)) {
                            targetObject = selectableChild;
                            break;
                        }
                    }
                }
                if (targetObject == null) { // not end on a child
                    this.state = IDLE;
                    return true;
                }
            }

            // update selected property and selection list
            targetObject.setInterimSelected(false);
            if (targetObject.contains(eventInGroup)) {
                if (!targetObject.isSelected()) { // if selected for the first time
                    targetObject.setSelected(true);
                    if (this.type == SINGLE) {
                        clearSelection();
                    }
                    selection.add(targetObject);
                } else { // if selected for a second time
                    if (this.type == MULTIPLE) {
                        targetObject.setSelected(false);
                        selection.remove(targetObject);
                    }
                }
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
            for (GraphicalObject child : group.getChildren()) {
                if (child instanceof SelectableGraphicalObject) {
                    ((SelectableGraphicalObject) child).setInterimSelected(false);
                    break;
                }
            }
            this.startInGroup = false;
            this.state = IDLE;
            return true;
        }
        return false;
    }

    public boolean check(BehaviorEvent event) {
        return start(event) || running(event) || stop(event) || cancel(event);
    }
}