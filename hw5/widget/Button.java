package widget;

import graphics.object.GraphicalObject;
import graphics.object.selectable.SelectableGraphicalObject;
import graphics.object.selectable.SelectableOutlineRect;

class Button implements SelectableGraphicalObject {
    private SelectableOutlineRect box;
    private GraphicalObject label;

    public Button(String label) {
    }

    public Button(GraphicalObject label) {
    }

    public SelectableOutlineRect getBox() {
        return this.box;
    }

    public GraphicalObject getLabel() {
        return this.label;
    }
}