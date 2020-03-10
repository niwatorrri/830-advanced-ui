package graphics.group;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ArrayList;

import graphics.object.GraphicalObject;
import graphics.object.BoundaryRectangle;
import graphics.object.AlreadyHasGroupRunTimeException;

public class LayoutGroup implements Group {
    private int x, y, width, height;
    private int layout, offset;
    private int nRows, nColumns;
    private Group group = null;
    private List<GraphicalObject> children
         = new ArrayList<GraphicalObject>();

    /**
     * Constructors
     * Caveat: user calls setLayout but did not provide required attributes
     */
    public LayoutGroup(int x, int y, int width, int height,
            int layout, int offset) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.layout = layout;
        this.offset = offset;

        checkIfSupportedLayout(layout);
        if ((layout != HORIZONTAL) && (layout != VERTICAL)) {
            throw new RuntimeException("Incorrect constructor");
        }
    }

    public LayoutGroup(int x, int y, int width, int height,
            int layout, int nRows, int nColumns) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.layout = layout;
        this.nRows = nRows;
        this.nColumns = nColumns;

        checkIfSupportedLayout(layout);
        if (layout != GRID) {
            throw new RuntimeException("Incorrect constructor");
        }
    }

    public LayoutGroup() {
        this(0, 0, 200, 200, HORIZONTAL, 10);
    }

    private void checkIfSupportedLayout(int layout) {
        if ((layout != HORIZONTAL) && (layout != VERTICAL) && (layout != GRID)) {
            throw new RuntimeException("Not supported layout type");
        }
    }

    /**
     * Getters and setters
     */
    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLayout() {
        return this.layout;
    }

    public void setLayout(int layout) {
        checkIfSupportedLayout(layout);
        this.layout = layout;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNRows() {
        return this.nRows;
    }

    public void setNRows(int nRows) {
        this.nRows = nRows;
    }

    public int getNColumns() {
        return this.nColumns;
    }

    public void setNColumns(int nColumns) {
        this.nColumns = nColumns;
    }

    /**
     * Methods defined in the GraphicalObject interface
     */
    public void draw(Graphics2D graphics, Shape clipShape) {
        // Intersect the clip shape with the group bounding box
        Shape commonClipArea = getBoundingBox().intersection(clipShape.getBounds());

        // Translate the new clip shape to pass to children
        AffineTransform transform = new AffineTransform();
        transform.translate(-x, -y);
        Shape childClipShape = transform.createTransformedShape(commonClipArea);

        // Translate the origin to draw children
        graphics.translate(x, y);
        int currentPosition = 0;
        int gridWidth = 0, gridHeight = 0;
        if (layout == GRID) {
            gridWidth = width / nColumns;
            gridHeight = height / nRows;
        }

        for (int idx = 0; idx < children.size(); ++idx) {
            GraphicalObject child = children.get(idx);
            BoundaryRectangle oldBoundingBox = child.getBoundingBox();
            if ((layout == GRID) && (idx >= nRows * nColumns)) {
                break;
            }

            switch (layout) {
                case HORIZONTAL:
                    child.moveTo(currentPosition, 0);
                    currentPosition += oldBoundingBox.width + offset;
                    break;
                case VERTICAL:
                    child.moveTo(0, currentPosition);
                    currentPosition += oldBoundingBox.height + offset;
                    break;
                case GRID:
                    child.moveTo(
                        (idx % nColumns) * gridWidth,
                        (idx / nColumns) * gridHeight
                    );
                    break;
                default:
                    throw new RuntimeException("Not supported layout type");
            }
            child.draw(graphics, childClipShape);
            child.moveTo(oldBoundingBox.x, oldBoundingBox.y);
        }
        graphics.translate(-x, -y);
    }

    public BoundaryRectangle getBoundingBox() {
        return new BoundaryRectangle(x, y, width, height);
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        if (this.group != null && group != null) {
            throw new AlreadyHasGroupRunTimeException();
        }
        this.group = group;
    }

    public boolean contains(int x, int y) {
        return getBoundingBox().contains(x, y);
    }

    /**
     * Methods defined in the Group interface
     */
    public void addChild(GraphicalObject child) {
        Group childGroup = child.getGroup();
        if (childGroup != null) {
            throw new AlreadyHasGroupRunTimeException();
        } else {
            children.add(child);
            child.setGroup(this);
        }
    }

    public void removeChild(GraphicalObject child) {
        children.remove(child);
        child.setGroup(null);
    }

    public void bringChildToFront(GraphicalObject child) {
        if (children.remove(child)) {
            children.add(child);
        } else {
            throw new RuntimeException("Object is not in the group");
        }
    }

    public void resizeToChildren() {
        int newWidth = 0, newHeight = 0;

        for (GraphicalObject child: children) {
            BoundaryRectangle boundingBox = child.getBoundingBox();
            if (layout == HORIZONTAL) {
                newWidth += boundingBox.width + offset;
                newHeight = Math.max(newHeight, boundingBox.height);
            } else if (layout == VERTICAL) {
                newHeight += boundingBox.height + offset;
                newWidth = Math.max(newWidth, boundingBox.width);
            } else if (layout == GRID) {
                newWidth = Math.max(newWidth, boundingBox.width * nColumns);
                newHeight = Math.max(newHeight, boundingBox.height * nRows);
            }
        }

        if (children.size() > 0) {
            if (layout == HORIZONTAL) {
                newWidth -= offset;
            } else if (layout == VERTICAL) {
                newHeight -= offset;
            }
        }
        this.width = newWidth;
        this.height = newHeight;
    }

    public List<GraphicalObject> getChildren() {
        List<GraphicalObject> childrenCopy
            = new ArrayList<GraphicalObject>(children);
        return childrenCopy;
    }

    public Point parentToChild(Point pt) {
        int childX = pt.x - x;
        int childY = pt.y - y;
        return new Point(childX, childY);
    }

    public Point childToParent(Point pt) {
        int parentX = pt.x + x;
        int parentY = pt.y + y;
        return new Point(parentX, parentY);
    }
}