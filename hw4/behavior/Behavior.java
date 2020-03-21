package behavior;

import graphics.group.Group;

public interface Behavior {
    public Group getGroup();
    public Behavior setGroup(Group group);

    public int getState();
    public static final int IDLE = 0;
    public static final int RUNNING_INSIDE = 1;
    public static final int RUNNING_OUTSIDE = 2;

    public BehaviorEvent getStartEvent();
    public Behavior setStartEvent(BehaviorEvent startEvent);

    public BehaviorEvent getStopEvent();
    public Behavior setStopEvent(BehaviorEvent stopEvent);

    public BehaviorEvent getCancelEvent();
    public Behavior setCancelEvent(BehaviorEvent cancelEvent);

    public boolean start(BehaviorEvent event);
    public boolean running(BehaviorEvent event);
    public boolean stop(BehaviorEvent event);
    public boolean cancel(BehaviorEvent event);
    public boolean check(BehaviorEvent event);
}