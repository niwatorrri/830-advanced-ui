# A Constraint System for an Object-Oriented Graphics System

Please include a readme file in .doc, .txt or .pdf format (note: not .md format) that explains how your constraint system works and how to use it. This document must describe:

## Constraint Solver

What solver you are using, including references. If you have changed the way the solver works from the documented algorithm, please describe the differences.

## Implementation

How you needed to change the design from HW2 to fit in your solver. For example, did you change the high-level definitions of graphical objects or their attributes? Also include notes on how you fit the solver into your implementation, so I can find it in your code files.

## Programming Guide

A programming guide that documents for a user of your API how to write a constraint. For example, if I want to write a custom constraint, what do I do?

To write a custom constraint, you need to follow the following structure:

```
    targetObject.setVariable(new Constraint<VariableType>(
        dependency1.useVariable(), dependency2.useVariable(), ...
    ) {
        public VariableType getValue() {
            return dependency1.getVariable() + dependency2.getVariable();
        }
    });
```

For example:

```
    redRect.setX(new Constraint<Integer>(blueRect.useX()) {
        public Integer getValue() {
            return blueRect.getX() + 50;
        }
    });
```

Note that the dependencies have to be accurately specified. If you pass more or fewer dependencies, the system might not work as expected.

I anticipate that this will only take a few pages, so it shouldn't be a big time sink.
