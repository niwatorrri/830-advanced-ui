package nodeeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

class Box {
    private int startX, startY, width, height;
    private String name;
    private LineStyle lineStyle;
    private Color lineColor;
    private Color faceColor;

    private boolean visible = true;
    private boolean selected = false;
    private String selectedHandle = null;

    private static final int HANDLE_WIDTH = 7;
    private static final int HANDLE_OFFSET = HANDLE_WIDTH / 2 + 1;

    public Box(int startX, int startY, int width, int height,
            String name, LineStyle lineStyle, Color lineColor, Color faceColor) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.name = name;
        this.lineStyle = lineStyle;
        this.lineColor = lineColor;
        this.faceColor = faceColor;
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

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
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

        // draw white interior
        g.setColor(faceColor);
        g.fillRect(startX, startY, width, height);

        // draw the outline
        this.lineStyle.use(g);
        g.setColor(lineColor);
        g.drawRect(startX, startY, width, height);

        /* Text format reference:
         * https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java */

        // show box name if any
        if (name != null) {
            int textX, textY;
            int upwardOffset = 2, downwardOffset = 12;

            FontMetrics metrics = g.getFontMetrics();
            int textWidth = metrics.stringWidth(name);
            int textHeight = metrics.getHeight();

            // place the text according to the box size
            textX = startX + (this.width - textWidth) / 2;
            if (textWidth > this.width || textHeight > this.height)
                textY = startY - upwardOffset;
            else
                textY = startY + downwardOffset;
            g.drawString(name, textX, textY);
        }
    }

    public void displayHandles(Graphics g) {
        /* Display the selection handles */
        if (!visible)
            return;

        int[][] handlePosition = {
            {startX, startY},
            {startX, startY + height / 2},
            {startX, startY + height},
            {startX + width / 2, startY},
            {startX + width / 2, startY + height},
            {startX + width, startY},
            {startX + width, startY + height / 2},
            {startX + width, startY + height},
        };

        g.setColor(Color.WHITE);
        for (int[] position: handlePosition)
            g.fillRect(position[0] - HANDLE_OFFSET, position[1] - HANDLE_OFFSET,
                    HANDLE_WIDTH, HANDLE_WIDTH);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g.setColor(lineColor);
        for (int[] position: handlePosition)
            g.drawRect(position[0] - HANDLE_OFFSET, position[1] - HANDLE_OFFSET,
                    HANDLE_WIDTH, HANDLE_WIDTH);
    }

    private boolean checkIfInvolved(MouseEvent e,
            int startX, int startY, int width, int height, int tolerance) {
        // check if a rectangle is involved in a mouse event (with tolerance)
        int mouseX = e.getX(), mouseY = e.getY();
        int endX = startX + width, endY = startY + height;
        return (mouseX >= startX - tolerance) && (mouseX <= endX + tolerance)
                && (mouseY >= startY - tolerance) && (mouseY <= endY + tolerance);
    }

    public boolean isInvolvedIn(MouseEvent e) {
        /* Check if the box is involved in a mouse event */
        if (!visible)
            return false;

        int tolerance = 5;
        return checkIfInvolved(e, startX, startY, width, height, tolerance);
    }

    public boolean handleIsInvoledIn(MouseEvent e) {
        return true;
    }
}