# A Constraint System for Retained Object Graphics System

[TOC]

## Constraint Solver

The constraint solver is mostly based on [Scott Hudson's lazy evaluation algorithm](https://dl.acm.org/doi/10.1145/117009.117012) as discussed in class. I made some differences to the algorithm so that it can detect cycles in the dependency graph and reports an error when a conflict in cyclic dependencies is found. Based on this feature, a multi-way constraint can also be expressed through several formulas, and the cycle in the graph will only be visited once to evaluate the constraints.

To be specific, I used a `visited` flag and adopted a depth-first search (DFS)-like method or backtracking to determine whether there exists a cycle in the dependency graph. In case user tries to set value to a constrained variable, this change will only be propagated through the network if this variable is involved in a cycle, since a cycle suggests a chance that a multi-way constraint can be successfully evaluated. I used another `evaluated` flag and a similar backtracking strategy to deal with cyclic constraint evaluation. If the `evaluated` flag becomes true before the whole evaluation finishes, a cycle is detected. If the value becomes different after it propagates along the cycle, then a constraint conflict is reported and user will be notified.

## Implementation

The constraint network is represented as a dependency graph, whose nodes are represented by `Dependency` objects and edges are represented by `Edge` objects, both defined in `Dependency.java`. To avoid introducing too many terms, I wrapped around the `Dependency` class so that the `Constraint` class serves as an alias of `Dependency` that is more familiar to users. The most important members of the `Dependency` class include out-of-date mark, list of incoming edges and list of outgoing edges. An `Edge` object represents a directed edge, including its start, its end (both are `Dependency` objects), and a pending mark. The constraint solver described in the previous section is basically implemented in `Constraint.evaluate()` (and `markOutOfDate()`). A `hasCycle()` method is also implemented to help detect cycles.

To fit this graph structure into the retained graphics system, all classes needs to be modified. Take `OutlineRect` class as an example. Each attribute has its own constraint as a class member, e.g., `xConstraint`, `yConstraint`, `colorConstraint`, etc., which are all instantiations of some kind of `Constraint<T>`. For each attribute, there is a getter `getX()`, two setters `setX()` that takes either a value or a new constraint, and a "user" `useX()` that returns the corresponding constraint member. Whenever there is a constraint, getter will evaluate the constraint, which might report constraint conflicts in `evaluate()`, and then return the evaluation result. When setter takes a constraint, the original constraint is replaced in the graph, and this change is notified to the outgoing neighbors. When setter takes a value, this value will only be accepted if the attribute is not constrained, or the attribute is involved in a cycle in the graph. If the value change is accepted, the neighbors are notified and the out-of-date marking process begins. The "user" is mostly for convenience of maintaining list of dependencies.

A subtle change is also made to the `GraphicalObject` interface implementations. Every time one wants to get an attribute, always use `get<Variable>` instead of directing accessing `this.<Variable>` because the current value might be out-of-date. Similarly, always use `set<Variable>` instead of `this.<Variable> = <Value>`, e.g., in `moveTo()` method.

## Programming Guide

To write a custom constraint on a target object, generally one needs to follow the following structure:

```java
  targetObject.setConstrainedVariable(new Constraint<ConstrainedVariableType>(
      anOptionalName,
      dependency1.useSomeVariable(), dependency2.useSomeVariable(), ...
  ) {
      public ConstrainedVariableType getValue() {
          return someFunction(dependency1.getSomeVariable(),
                              dependency2.getSomeVariable(), ...);
      }
  });
```

For example:

```java
  redRect.setX(new Constraint<Integer>(
      "redRect.x",
      blueRect.useX()
  ) {
      public Integer getValue() {
          return blueRect.getX() + 50;
      }
  });
```

This statement sets up a constraint `redRect.x = blueRect.x + 50`. Note that the dependencies MUST be accurately specified. If you pass fewer dependencies than needed, the system will not work as expected. The `getValue()` method in the anonymous class can be implemented to realize arbitrary equality constraints. The solver is robust against exceptions that occur in `getValue()`. The name for the constraint is optional, but specifying an understandable name can help you see better if an error is reported from the constraint solver.

Another example:

```java
  Text sizeText = new Text();
  sizeText.setText(new Constraint<String>(
      blueRect.useWidth(), blueRect.useHeight()
  ) {
      public String getValue() {
          return "width=" + String.valueOf(blueRect.getWidth()) + " " +
                 "height=" + String.valueOf(blueRect.getHeight());
      }
  });
```

This statement sets up a constraint `sizeText.text = 'width=<blueRect.width> height=<blueRect.height>'`. Note that the constrained variable can be of arbitrary type, including `String` . A constraint name is not specified here. This constraint involves two dependencies as specified in the constructor arguments.

To express multi-way constraints, you have to specify formula for each way you want tu support. A multi-way constraint involves cycles, and it can be successfully evaluated only if the given formulas are consistent, i.e., no contradiction exists. Otherwise, an error will be output to stderr. Here is an example:

```java
  redRect.setX(new Constraint<Integer>(
      blueRect.useX(), blueRect.useWidth()
  ) {
      public Integer getValue() {
          return blueRect.getX() + blueRect.getWidth();
      }
  });
  
  blueRect.setX(new Constraint<Integer>(
      redRect.useX(), blueRect.useWidth()
  ) {
      public Integer getValue() {
          return redRect.getX() - blueRect.getWidth();
      }
  });
```

These statements set up a multi-way constraint `redRect.x = blueRect.x + blueRect.width` that supports two one-way constraints, `redRect.x <- blueRect.x + blueRect.width` and `blueRect.x <- redRect.x - blueRect.width`. Since these two constraints are consistent, setting `redRect.x` will successfully change `blueRect.x` as well.

For more examples and features, see `TestHomework3.java`.

## Miscellaneous

1. Fixed the bug of `Graphics.drawString` unable to handle newlines `\n` in text.
2. Fixed grid layout behavior in LayoutGroup.
3. Maybe consider Java Reflection API to make writing things (e.g., subclasses) and code refactoring easier, but should also consider its performance overhead. Didn't try this but one potential issue is the indefinite return type from getVariable.
