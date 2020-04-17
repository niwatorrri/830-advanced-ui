package ui.toolkit.graphics.object.selectable;

public interface Selectable {
     public boolean isInterimSelected();
     public boolean isSelected();
     public void setInterimSelected(boolean interimSelected);
     public void setSelected(boolean selected);
}