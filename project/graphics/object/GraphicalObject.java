package graphics.object;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import constraint.Constraint;
import graphics.group.Group;

public interface GraphicalObject {
    public void draw(Graphics2D graphics, Shape clipShape);
    public BoundaryRectangle getBoundingBox();
    public void moveTo(int x, int y);
    public Group getGroup();
    public void setGroup(Group group);
    public boolean contains(int x, int y);
    public boolean contains(Point pt);

    // Setup position constraints for widget components
    public void setX(Constraint<Integer> constraint);
    public void setY(Constraint<Integer> constraint);
}