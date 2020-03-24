# An Input Model for Retained Object Graphics System

[TOC]

## Introduction

This homework adds input handling to the retained object graphics system implemented in previous homeworks. We will be using the Garnet-Amulet behavior object model. The behaviors supported include Move, Choice and New. All of the source codes have been organized into the following hierarchy.

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
 |___ TestAllBehaviors.java
 |___ README.md
```

Implementations for graphical objects and constraints mostly remain unchanged. Behavior classes implementation is basically independent of graphical objects and constraints. For the choice behavior to work, each graphical object class (e.g., OutlineRect) has a corresponding selectable subclass (e.g., SelectableOutlineRect) that implements the Selectable interface. The behavior.InteractiveWindowGroup class is the main entry point that listens for mouse and keyboard input, upon which converts it to a BehaviorEvent and checks if any Behavior needs to change state on this event. After states are determined, InteractiveWindowGroup will call redraw to change the appearance of the graphical objects if any, whose attributes may be subject to constraints that users have defined, and they are solved with the constraint solver.

## Programming Guide

## Change Log

This section records some changes that I made either to implementations in previous homeworks, or to the specifications of this homework.

- Behavior classes
    - Provided fluent API in setters for method cascading
    - CancelEvent is customizable
    - MoveBehavior supports a gridSize parameter to move objects in grid coordinates
    - NewBehavior constructor is changed to `NewBehavior(rectLike, constraint)`. The onePoint parameter is removed since I call `make(x, y, x, y)` for both line-like objects and one-point objects. The constraint parameter is setup for the new object when it is created in make method (See the Constraints part for more information).
    - NewBehavior has an abstract method named `isTrivial(object)` to tell if the new object is zero-sized when the behavior stops. If yes, it will be removed from group.
    - NewBehavior by default makes selectable objects so that it can be cast to any of their super-types.
    - NewRectBehavior supports three types of objects: OutlineRect, FilledRect and Ellipse
- BehaviorEvent classes
    - Constructor changed to `BehaviorEvent(modifiers, key, id)` as it reads more natural to me
    - Constructor accepts char (e.g., 'A', case-insensitive) as key code for KeyEvents
    - Event ID supports mouse move and mouse drag
    - Event match supports ANY_MODIFIER
    - Windows key modifier and Function key modifier are not supported, as I cannot find them in java.awt.event.InputEvent. They seem to be considered as normal keys instead of modifiers since they (to be specific, Windows, F1, F2, F3, etc.) can be found in java.awt.event.KeyEvent
- InteractiveWindowGroup class
    - Supports multiple behaviors react to one single event (haven't fully tested, might be messy)
    - When a keyboard event occurs, the x and y parameters in the resulting BehaviorEvent object is the current mouse position. The `start()` method in Behavior will consider this position as well.
- Constraints
    - Declared Constraint and Dependency as abstract classes and force user to implement `getValue()`. NoConstraint class can be instantiated for a default initializer.
    - Added a SetupConstraint functional interface to help pass the constraint setup process as a parameter to NewBehavior. This also **happens to fix the problem of the original Constraint implementation** unable to handle dynamically created objects. See Programming Guide or TestAllBehavior.java for examples.
- Graphical objects
    - Added SelectableGraphicalObject and SelectableGroup interfaces as publicly available types.
    - Added a fluent API for addChild and removeChild method in Group interface, as well as addChildren and removeChildren methods for lazy people. Maybe also add for GraphicalObject later.
    - Enabled text anti-aliasing in Text class, always.
    - Fixed bugs in Text class so that sometimes it is okay not to pass a Graphics2D object (or pass a null) to the constructor.
    - Fixed a bug in grid layout of LayoutGroup that children are moved back to original position after being drawn.