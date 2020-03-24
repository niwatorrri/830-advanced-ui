# An Input Model for Retained Object Graphics System

[TOC]

## Introduction

This homework adds input handling to the retained object graphics system implemented in previous homeworks. We will be using the Garnet-Amulet behavior object model. The behaviors supported include Move, Choice and New, and New behavior further supports NewRect, NewLine and NewText. All of the source codes have been organized into the following hierarchy.

```
hw4
 |___ behavior
 |       |___ Behavior.java, etc.
 |___ constraint
 |       |___ Constraint.java, etc.
 |___ graphics
 |       |___ group
 |       |       |___ selectable
 |       |       |       |___ SelectableSimpleGroup.java, etc.
 |       |       |___ SimpleGroup.java, etc.
 |       |___ object
 |               |___ selectable
 |               |       |___ SelectableOutlineRect.java, etc.
 |               |___ OutlineRect.java, etc.
 |___ README.md
 |___ TestAllBehaviors.java
 |___ MyInteractiveWindow.java (will introduce later)
```

Implementations for graphical objects and constraints mostly remain unchanged. Behavior classes implementation is basically independent of graphical objects and constraints. For the choice behavior to work, each graphical object class (e.g., OutlineRect) has a corresponding selectable subclass (e.g., SelectableOutlineRect) that implements the Selectable interface. The behavior.InteractiveWindowGroup class is the main entry point that listens for mouse and keyboard input, upon which the window converts it to a BehaviorEvent and checks if any Behavior needs to change state on this event. After states are determined, the window will call redraw to change the appearance of the graphical objects if any, whose attributes may be subject to constraints that users have defined, and whose values are then solved with the constraint solver.

## Programming Guide

### Tutorial

To create an interactive window where behaviors can be attached to graphical objects, you should extend the `behavior.InteractiveWindowGroup` class and attach a top group to the window which should contain all of your graphical objects, demonstrated as follows.

```java
public class MyInteractiveWindow extends InteractiveWindowGroup {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;

    public static void main(String[] args) {
        new MyInteractiveWindow();
    }

    public MyInteractiveWindow() {
        super("MyInteractiveWindow", WINDOW_WIDTH, WINDOW_HEIGHT);
        Group topGroup = new SimpleGroup(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        addChild(topGroup);
        writeMyInteractions(topGroup);
        redraw(topGroup);
    }
}
```

All graphical objects and behaviors will be created and interacting each other as defined in the `writeMyInteraction` method. Let's create a simple group with a couple of graphical objects, and attach a `MoveBehavior` object to this group.

```java
    Group moveGroup = new SimpleGroup(0, 0, 300, 300)
        .addChildren(
            new OutlineRect(20, 20, 50, 80, Color.BLACK, 1), 
            new Text("Movable Text", 150, 80)
        );
    topGroup.addChild(moveGroup);
    addBehavior(new MoveBehavior().setGroup(moveGroup));
```

Note that `Group.addChildren` method and `Behavior` fluent API are available to save lines of code. (`InteractiveWindowGroup.addBehaviors` is also available.) And we are done! Now you can move your objects, a rectangle and a text, with your mouse. Move behavior by default starts with mouse pressed, and stops with mouse released. Pressing the Esc key will cancel the behavior and the object returns to its original position. These default triggers are the same for all behaviors, but can be customized. For example,

```java
    addBehavior(
        new MoveBehavior()
            .setGroup(moveGroup)
            .setStartEvent(new BehaviorEvent(SHIFT_MODIFIER, LEFT_MOUSE_KEY, MOUSE_DOWN_ID))
            .setStopEvent(new BehaviorEvent(ANY_MODIFIER, LEFT_MOUSE_KEY, MOUSE_UP_ID))
            .setCancelEvent(new BehaviorEvent(NO_MODIFIER, 'z', KEY_UP_ID))
    );
```

With this, move will only start on Shift plus left mouse down and stop on left mouse up (regardless of modifiers). The move can be cancelled while running by pressing the 'Z' key on the keyboard. A gridSize parameter is optional for MoveBehavior constructor to force objects to move along grid coordinates.

Now let's try a `ChoiceBehavior` attached to some selectable graphical objects, with a constraint that changes their appearance based on whether they are selected and/or interimSelected.

```java
    // Create a group that has a couple of selectable children
    Group choiceGroup = new SimpleGroup(0, 0, 300, 300)
        .addChildren(
            new SelectableOutlineRect(20, 20, 50, 80, Color.BLACK, 1),
            new SelectableOutlineRect(80, 80, 50, 80, Color.BLACK, 1),
        );
    topGroup.addChild(choiceGroup);

    // Use SetupConstraint interface to setup constraint for every object
    SetupConstraint<SelectableOutlineRect> rectColorConstraint = o -> {
        o.setColor(new Constraint<Color>(o.useSelected(), o.useInterimSelected()) {
            public Color getValue() {
                if (o.isSelected()) {
                    return o.isInterimSelected() ? Color.BLUE : Color.GREEN;
                } else {
                    return o.isInterimSelected() ? Color.YELLOW : Color.BLACK;
                }
            }
        });
    };
    for (GraphicalObject o : choiceGroup.getChildren()) {
        rectColorConstraint.setup((SelectableOutlineRect) o);
    }

    // Attach a choice behavior to the group
    addBehavior(new ChoiceBehavior(ChoiceBehavior.MULTIPLE, false).setGroup(choiceGroup));
```

The `SetupConstraint` is a functional interface newly introduced in this homework's constraint implementation. It allows setup constraint for dynamically created objects, which cannot be done with previous implementation, and also it helps with passing constraint setup as a parameter. The snippet above attaches a multiple choice behavior to the group, and the rectangles will change their color according to the constraint. We finally show an example of `NewBehavior` attached to a group.

```java
    Group newRectGroup = new SimpleGroup(0, 0, 300, 300);
    topGroup.addChild(newRectGroup);
    addBehavior(
        new NewRectBehavior(NewRectBehavior.OUTLINE_RECT, Color.BLACK, 2)
            .setGroup(newRectGroup)
    );
```

The `NewBehavior` constructor takes an optional parameter of type `SetupConstraint<?>`, so that some constraint(s) can be set up for the new objects upon creation. A usage example is given in the next subsection.

### A Complete Example with Advanced Features

In this subsection, we provide a complete example, where users are allowed to make new rectangles, move them by click and drag, and choose them with a single choice behavior. The code is in `MyInteractiveWindow.java`.

```java
    Group g = new SimpleGroup(0, 0, 400, 400);
    topGroup.addChild(g);

    SetupConstraint<SelectableOutlineRect> rectColorConstraint = o -> {
        o.setColor(new Constraint<Color>(o.useSelected(), o.useInterimSelected()) {
            public Color getValue() {
                if (o.isSelected()) {
                    return o.isInterimSelected() ? Color.BLUE : Color.GREEN;
                } else {
                    return o.isInterimSelected() ? Color.YELLOW : Color.BLACK;
                }
            }
        });
    };
    for (GraphicalObject o : g.getChildren()) {
        rectColorConstraint.setup((SelectableOutlineRect) o);
    }

    addBehaviors(
        new MoveBehavior().setGroup(g).setPriority(0),
        new ChoiceBehavior(ChoiceBehavior.SINGLE, true).setGroup(g).setPriority(0),
        new NewRectBehavior(
            NewRectBehavior.OUTLINE_RECT, Color.BLACK, 2, rectColorConstraint).setGroup(g)    
    );
```

The first feature here is the additional parameter that `NewBehavior` takes. A `SetupConstraint<?>` type variable is passed to `NewRectBehavior` constructor, so that the constraint will be set up when the behavior starts and call the make method.

The second feature here is behavior priority. Every behavior can be assigned a priority, a non-negative integer. If not explicitly set, they will be assigned when the behaviors are added to the window, with the value of 0, 1, 2, ..., respectively. The lower the value, the higher the priority. Behaviors that share the same priority can simultaneously react to one input event. If the event is consumed by a behavior of higher priority, it cannot be consumed by a behavior of lower priority any more. The example above sets the priority of move and choice behavior both to 0, and the new rect behavior will have priority 1 when added. This allows users to both move and select at the same time, and new rects won't be created when the mouse is moving an existing rect.

## Change Log

This section records some changes that I made either to implementations in previous homeworks, or to the specifications of this homework.

- Behavior classes
    - Provided fluent API in setters for method cascading
    - CancelEvent is customizable
    - Added a priority member for InteractiveWindowGroup
    - MoveBehavior supports a gridSize parameter to move objects in grid coordinates
    - NewBehavior constructor is changed to `NewBehavior(rectLike, constraint)`. The onePoint parameter is removed since I call `make(x, y, x, y)` for both line-like objects and one-point objects. The constraint parameter is to setup a constraint for the new object when it is created in make method. (See the Constraints part for more information.)
    - NewBehavior has an abstract method named `isTrivial(object)` to tell if the new object is zero-sized when the behavior stops. If yes, it will be removed from the group.
    - NewBehavior by default makes selectable objects so that it can be cast to any of their super-types.
    - NewRectBehavior supports three types of objects: OutlineRect, FilledRect and Ellipse
- BehaviorEvent classes
    - Constructor changed to `BehaviorEvent(modifiers, key, id)` as it reads more natural to me
    - Constructor accepts char (e.g., 'A', case-insensitive) as key code for KeyEvents
    - Event ID supports mouse move and mouse drag
    - Event match supports ANY_MODIFIER
    - Windows key modifier and Function key modifier are not supported, as I cannot find them in java.awt.event.InputEvent. They seem to be considered as normal keys instead of modifiers since they, (to be specific, Windows, F1, F2, F3, etc.,) can only be found in java.awt.event.KeyEvent.
- InteractiveWindowGroup class
    - Added a fluent API for addBehavior and removeBehavior method in InteractiveWindowGroup class, as well as addBehaviors and removeBehaviors methods for lazy people.
    - Behaviors can have priorities, and multiple behaviors that share the same priority can simultaneously react on one input event. (Haven't fully tested, might be messy.)
    - When a keyboard event occurs, the x and y parameters in the resulting BehaviorEvent object is the current mouse position. The `start()` method in Behavior will consider this position as well.
- Constraints
    - Declared Constraint and Dependency as abstract classes and force user to implement `getValue()`. NoConstraint class can be instantiated for a default initializer.
    - Added a SetupConstraint functional interface to help pass the constraint setup process as a parameter to NewBehavior. This also **happens to fix the problem of the original Constraint implementation** that it's unable to handle dynamically created objects. See Programming Guide or TestAllBehaviors.java for examples.
- Graphical objects
    - Added SelectableGraphicalObject and SelectableGroup interfaces as publicly available types.
    - Added a fluent API for addChild and removeChild method in Group interface, as well as addChildren and removeChildren methods for lazy people. Maybe also add for GraphicalObject later.
    - Enabled text anti-aliasing in Text class, always.
    - Fixed bugs in Text class so that: 1) text are drawn at correct positions when there is a newline `\n` character; and 2) sometimes it is okay not to pass a Graphics2D object (or pass a null) to the constructor.
    - Fixed a bug in grid layout of LayoutGroup that children are moved back to original position after being drawn.