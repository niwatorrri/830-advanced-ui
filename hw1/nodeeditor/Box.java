package nodeeditor;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

class Box {
    private int startX, startY, width, height;
    private String name;
    private LineStyle lineStyle;
    private boolean visible;
    private boolean selected;

    public Box(int startX, int startY, int width, int height,
            String name, LineStyle lineStyle) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.name = name;
        this.lineStyle = lineStyle;
        this.visible = true;
        this.selected = false;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public LineStyle getLineStyle() {
        return lineStyle;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setStart(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public void setVisible(boolean bool) {
        visible = bool;
    }

    public void setSelected(boolean bool) {
        selected = bool;
    }

    public void display(Graphics g) {
        /* Display the box on the panel */
        if (!visible)
            return;

        g.setColor(Color.WHITE);
        g.fillRect(startX, startY, width, height);

        this.lineStyle.use(g);
        if (!selected)
            g.setColor(Color.BLACK);
        else 
            g.setColor(Color.RED);
        g.drawRect(startX, startY, width, height);

        /* Text format reference:
         * https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java */

        if (name != null) {
            int textX, textY;
            int UPWARD_OFFSET = 2, DOWNWARD_OFFSET = 12;

            FontMetrics metrics = g.getFontMetrics();
            int textWidth = metrics.stringWidth(name);
            int textHeight = metrics.getHeight();

            textX = startX + (this.width - textWidth) / 2;
            if (textWidth > this.width || textHeight > this.height)
                textY = startY - UPWARD_OFFSET;
            else
                textY = startY + DOWNWARD_OFFSET;
            g.drawString(name, textX, textY);
        }
    }

    public boolean isClickedBy(MouseEvent e) {
        /* Check if the box is clicked by mouse */
        if (!visible)
            return false;

        int clickedX = e.getX(), clickedY = e.getY();
        int endX = startX + width, endY = startY + height;
        int TOLERANCE = 5;
        return (clickedX >= startX - TOLERANCE) && (clickedX <= endX + TOLERANCE)
                && (clickedY >= startY - TOLERANCE) && (clickedY <= endY + TOLERANCE);
    }
}
