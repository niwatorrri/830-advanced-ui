package graphics.object;

import java.awt.Color;

public interface Rect extends GraphicalObject {
    public int getX();
    public int getY();
    public int getWidth();
    public int getHeight();
    public Color getColor();

    public void setX(int x);
    public void setY(int y);
    public void setWidth(int width);
    public void setHeight(int height);
    public void setColor(Color color);
}