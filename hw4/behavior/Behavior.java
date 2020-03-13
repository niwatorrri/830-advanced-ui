package behavior;

import graphics.group.Group;

public interface Behavior {
    public Group getGroup();
    public void setGroup(Group group);

    public int getState();
    public static final int IDLE = 0;
    public static final int RUNNING_INSIDE = 1;
    public static final int RUNNING_OUTSIDE = 2;

    public BehaviorEvent getStartEvent();
    public void setStartEvent(BehaviorEvent startEvent);

    public BehaviorEvent getStopEvent();
    public void setStopEvent(BehaviorEvent stopEvent);

    public Boolean start(BehaviorEvent event);
    public Boolean running(BehaviorEvent event);
    public Boolean stop(BehaviorEvent event);
    public Boolean cancel(BehaviorEvent event);
}