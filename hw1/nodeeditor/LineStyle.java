package nodeeditor;

import java.awt.*;
import java.awt.event.MouseEvent;

class LineStyle {
    private Stroke lineStyle;
    private int startX, startY, endX, endY; // position displayed on the panel
    private boolean selected;

    /* Line style reference:
     * https://www.codejava.net/java-se/graphics/drawing-lines-examples-with-graphics2d */
    private static int lineLength = 40, separateLine = 150;
    private static int lineStartX = (separateLine - lineLength) / 2;
    private static int lineEndX = (separateLine + lineLength) / 2;
    private static int[] lineY = {60, 90, 120, 150};
    private static float[] dashedPattern = {2f, 2f};
    private static float[] dashedDottedPattern = {10f, 10f, 1f, 10f};

    // set default line position and style
    public static LineStyle[] defaultLineStyles = {
        new LineStyle(new BasicStroke(2f), lineStartX, lineY[0], lineEndX, lineY[0]),
        new LineStyle(new BasicStroke(4f), lineStartX, lineY[1], lineEndX, lineY[1]),
        new LineStyle(new BasicStroke(2f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dashedPattern, 2.0f),
                lineStartX, lineY[2], lineEndX, lineY[2]),
        new LineStyle(new BasicStroke(3f, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER, 1.0f, dashedDottedPattern, 0.0f),
                lineStartX, lineY[3], lineEndX, lineY[3])
    };

    public LineStyle(Stroke lineStyle, int startX, int startY, int endX, int endY) {
        this.lineStyle = lineStyle;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.selected = false;
    }

    public void setSelected(boolean bool) {
        this.selected = bool;
    }

    public void display(Graphics g) {
        /* Display the line style on the panel */
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(lineStyle);
        g2.setColor(Color.BLACK);
        g2.drawLine(startX, startY, endX, endY);

        // draw a rectangle around the line style if it is selected
        if (selected) {
            int OFFSET = 5;
            int width = endX - startX + 2 * OFFSET;
            int height = endY - startY + 2 * OFFSET;
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(startX - OFFSET, startY - OFFSET, width, height);
        }
    }

    public void use(Graphics g) {
        /* Use the current line style */
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(lineStyle);
    }

    public boolean isInvolvedIn(MouseEvent e) {
        /* Check if the line style is involved in a mouse event (with tolerance) */		
        int mouseX = e.getX(), mouseY = e.getY();
        int TOLERANCE = 10;
        return (mouseX >= startX - TOLERANCE) && (mouseX <= endX + TOLERANCE)
                && (mouseY >= startY - TOLERANCE) && (mouseY <= endY + TOLERANCE);
    }
}