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

    public BehaviorEvent getCancelEvent();
    public void setCancelEvent(BehaviorEvent cancelEvent);

    public boolean start(BehaviorEvent event);
    public boolean running(BehaviorEvent event);
    public boolean stop(BehaviorEvent event);
    public boolean cancel(BehaviorEvent event);
    public boolean check(BehaviorEvent event);
}