package nodeeditor;

import java.awt.*;
import java.awt.event.MouseEvent;

class LineStyle {
    private Stroke lineStyle;
    private int startX, startY, endX, endY;
    private boolean selected;

    /* Line style reference:
     * https://www.codejava.net/java-se/graphics/drawing-lines-examples-with-graphics2d */
    private static float[] dashedPattern = {2f, 2f};
    private static float[] dashedDottedPattern = {10f, 10f, 1f, 10f};

    public static LineStyle[] defaultLineStyles = {
        new LineStyle(new BasicStroke(2f), 30, 20, 60, 20),
        new LineStyle(new BasicStroke(4f), 30, 40, 60, 40),
        new LineStyle(new BasicStroke(2f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dashedPattern, 2.0f), 30, 60, 60, 60),
        new LineStyle(new BasicStroke(3f, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER, 1.0f, dashedDottedPattern, 0.0f), 30, 80, 60, 80)
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
        g2.drawLine(startX, startY, endX, endY);

        // draw a rectangle if selected
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

    public boolean isClickedBy(MouseEvent e) {
        /* Check if the line style is clicked by mouse */		
        int clickedX = e.getX(), clickedY = e.getY();
        int TOLERANCE = 10;
        return (clickedX >= startX - TOLERANCE) && (clickedX <= endX + TOLERANCE)
                && (clickedY >= startY - TOLERANCE) && (clickedY <= endY + TOLERANCE);
    }
}
