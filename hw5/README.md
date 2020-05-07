# Widgets and My Node Editor

[TOC]

## Programming Guide

In the `widget` package, the basic widgets available include `ButtonPanel`, `CheckBoxPanel`, `RadioButtonPanel` and `NumberSlider`. They all extend a `Widget<T>` class that implements `Group` interface, where T is the type of the widget value. For the first three widgets, there are also three corresponding basic components, `Button`, `CheckBox` and `RadioButton`.

Let's start with `ButtonPanel` in detail. An example:

```java
    iHaveThreeButtons =
        new ButtonPanel(0, 0, true, ButtonPanel.SINGLE)
            .addChildren(
                new Button("First button"),
                new Button(new Text("Second Button")),
                new Button(new FilledEllipse())
            )
            .setCallback(v -> {
                System.out.println("New selected button: " + v.get(0));
            });
```

The constructor `ButtonPanel(x, y, finalFeedback, selectionType, layout, offset)` takes six parameters in total, first two being the position, the third for whether the button selection state is persistent, the fourth for whether multiple selection is allowed, the last two specifying the widget layout. Setting `finalFeedback=false` is suitable for one-time actions, e.g., delete. If `finalFeedback=true` is set, single choice is suitable for mutually exclusive options, e.g., spinning clockwise or counter-clockwise, while multiple choice is more appropriate for options that can co-exist, e.g., a list of to-do items.

The `ButtonPanel` class can only accept `Button` instances as its children. `Button` can take a string or a `GraphicalObject` for the constructor. These will serve as labels for buttons and be automatically placed in a box. Both sub-components are available through getters. If you are unhappy with the auto-alignment, you can use `Button.setAlignment` method to setup your custom alignment constraints.

Each widget has a value. The value for `ButtonPanel` is of type `List<Button>`. Values can be imposed constraints on to depend on other objects and attributes. Callbacks are called when the widget value has been changed. The default callback does nothing, and you can customize this behavior by setting callback using a lambda function that takes the new value as the argument.

The `CheckBoxPanel` and `RadioButtonPanel` work in a similar fashion, except that their choice behaviors are different. `CheckBoxPanel` only allows multiple choice while `RadioButtonPanel` only allows single choice, both always with persistent selection states. The types of their value are `List<CheckBox>` and `RadioButton` respectively.

Number sliders are more appropriate for numerical values. The `NumberSlider` class is a little different in its constructor: `NumberSlider(x, y, minValue, maxValue, increment, layout)` takes positions, value range, standard increment as well as layout as its arguments. A `NumberSlider` instance includes a bar, a slider (in the shape of a circle), two buttons to adjust value, and a text displaying the value. To change the value, either click the buttons on either side, or use mouse to drag the slider. A simple example: 

```java
    addChildren(
        new RadioButtonPanel().addChildren(
            new RadioButton("A Radio Button"),
            new RadioButton(new Ellipse())
        ),
        new CheckBoxPanel(200, 100).addChildren(
            new CheckBox("A Checkbox Item"),
            new CheckBox(new FilledRect(0, 0, 20, 30, Color.GREEN))
        ),
        new NumberSlider(200, 200, 0, 100, 1, NumberSlider.HORIZONTAL_LAYOUT)
    );
```

## Discussion

### Time and Lines of Code

1. Time for reading the documentation: 0 hrs

2. Time thinking about the design: 1 hr 40 mins

3. Time actually typing the code: 1 hr 20 mins

4. Time to look up things while coding: 20 mins

5. Time debugging to make it work: 1 hr

6. Time refactoring code and adding functionalities: 1 hr 30 mins

Though this is by no means accurate, compared with homework 1, I spent far less time completing the editor implementation. (Current: < 6 hrs; Previous: 17 hrs.) The time spent on reading documentations is 0 hrs now, since I'm the one who wrote them all :). Time spent looking up things has also become shorter, and I believe that's because the huge amount of implementation and object-oriented design work in this course has made me somewhat of a Java expert. The difference in the total time is also partly due to the extra credit I did for hw1.

Time typing the code is far less and more time is spent on thinking about design: In homework 1, I used immediate mode graphics and therefore everything is reduced to basic math and is pretty straightforward. However, after we have introduced retained graphics, constraints and interactor input model, the way it functions becomes a little opaque and complex to reason about when writing code and debugging. This should lead to an increase in design time. But the good thing is---I'm familiar with every implementation I've done, so I'm able to pinpoint the bugs fairly quickly and reorganize some methods as appropriate while implementing the editor.

Total lines of code for the new editor is 160, counted by LineCount 0.1.7 for Visual Studio Code. Previous was 525 LOC. This is a significant reduce in the coding effort, thanks to the large amount of code implemented under the hood. Encapsulation is well done so that users only need to focus on their high-level interactions and don't need to care about the graphic details.

### Difficulty Evaluation

When I was doing hw4, I allowed multiple behaviors to run upon one behavior event and introduced behavior priorities, which made the composition of move + choice + new behaviors pretty easy. The graphics part is pretty straightforward. However, constraints, behaviors and callbacks are the three most important as well as complex interaction techniques. Tasks like centering of box names, showing a different appearance for a selected box, etc., are fairly easy. The rest requires much thinking and drives me to restructure my code a great deal.

For requirement 2-4 where new boxes should have the selected linestyle, I added a linestyle constraint on the NewBoxBehavior that depends on the linestyle widget value, which works pretty smoothly. For requirement 2-9 where a box can be deleted on button click, I took advantage of the customized callback method for the button panel. This finally shed light for me on how callbacks should work in the current implementation. For requirement 2-7 where the linestyle menu should show the linestyle of the selected box, I think this is where things can be improved. This cannot be done using constraints in my implementation because neither the NewBoxBehavior itself nor its associated group provides a constraint on its children. (These can be added later if necessary, but should be slightly more involved.) This also cannot be done using callbacks, since the code flow should go from the new box group to linestyle menu, and the group does not have callbacks supported. Therefore, I turned to using behaviors and overrode the stop method in NewBoxBehavior to capture this. This doesn't look very good to me, as it requires knowledge into the implementation of behavior themselves.

So I think the process of deciding a proper interaction technique between components, using constraints, behaviors or callbacks, is an inevitable headache. But once you are done with that, only very few lines of code will be sufficient to achieve that. To reduce load on programmers' minds, I'm thinking maybe we can view constraints as a type of callbacks, where we first get dependency values and then call back to compute the target value (assuming equality constraints). Behaviors can also have callbacks so that once they stop the callback will be automatically called if need be. Callbacks in the most general sense define a code flow among the components. If we can somewhat combine these three techniques into one, that should make the programming experience even smoother.

Other things that might worth mentioning: a) I tried to center the labels inside buttons but I could not find a satisfactory design choice. The reason for this is that in `setX(constraint)`, centering requires a call to `getBoundingBox()`, which in turn calls `getX()` to get the latest value, which again, since x is constrained, calls the constraint evaluation as passed to `setX(constraint)`. This leads to an infinite loop. I've been thinking hard on this but don't have a clue on how to handle this in an elegant way. b) Sometimes I encountered concurrency issues while testing my implementation, and I don't think I would fix them anyway. c) It is quite easy to leave out necessary dependencies when writing constraints. It would be nice if there could be a way to detect this.

## Change Log

Here we record some changes to the previous implementations (plus some TODOs). Ones marked with (*) are good for users to know.

Widget classes
- Calling `addChild` or `addChildren` will be automatically followed by a call to `resizeToChildren`.
- TODO: vertical layout for NumberSlider
- TODO: resize buttons to have the same width/height
- TODO: more reasonable alignment constraints for buttons (Button, CheckBox, RadioButton)
- TODO: support customizable selection constraint on widget components
- TODO: more accurate constraint dependencies in widgets

Behavior classes
- Added Javadoc comments for all behavior constructors
- Separated a NewEllipseBehavior class from the NewRectBehavior class, and the former now has a new option FILLED_ELLIPSE besides ELLIPSE.
- (*) Added constraints on the attributes of NewBehavior subclasses

InteractiveWindowGroup class
- Since the window can only have one top group, the `topGroup` variable has now been incorporated into the InteractiveWindowGroup class so that subclasses will only need to call `addChild()` instead of `topGroup.addChild()`. (Some bugs were fixed while we were developing our final project.)

Constraint class
- Fixed a bug in `Dependency.evaluate()` so that it is okay to evaluate a constraint chain in an out-of-order fashion. This happens when one dependency doesn't get auto-evaluated on redraw, e.g., value in widgets.
- (*) One caveat when setting up multi-way constraint (with multiple consistent one-way constraints): should set one constraint to be not out-of-date, or else the solver won't know where to begin with.

Groups and Graphical objects
- (*) Groups now have a member named `behaviors` that collect all behaviors that are attached to them. These behaviors will also be automatically added to the top group when the group is added to a parent.
- On removing a group child, there is one assumption I made when implementing auto-removing its associated behaviors: the whole group hierarchy will be already established at the time of removal.
- Added FilledEllipse class and SelectableFilledEllipse class and implemented a more precise `contains()` method for ellipses.
- (*) For Text class, there is no need to pass a Graphics2D object to the constructor any more. Methods in the java.awt.Font class will take care of the string bound computation.
- Added getters for text-specific attributes, including ascent, descent, leading, text width and text height.
- Fixed a bug in Text class so that the bounding box will now take into account the newline characters `\n` in the text string.
- TODO: refine how the new attribute `invariant` in Line class affects other methods
