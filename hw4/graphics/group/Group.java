package graphics.group;

import java.awt.Point;
import java.util.List;

import graphics.object.GraphicalObject;
import graphics.object.AlreadyHasGroupRunTimeException;

public interface Group extends GraphicalObject {
    public Group addChild(GraphicalObject child) throws AlreadyHasGroupRunTimeException;
    public Group removeChild(GraphicalObject child);
    public Group bringChildToFront(GraphicalObject child);
    public Group resizeToChildren();
    public List<GraphicalObject> getChildren();
    public Point parentToChild(Point pt);
    public Point childToParent(Point pt);
}
