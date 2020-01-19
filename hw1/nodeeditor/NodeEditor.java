package nodeeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/* 
 * Starter code references: Java Swing Tutorial written by Oracle
 * SwingPaintDemo3: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/painting/SwingPaintDemo3Project/src/painting/SwingPaintDemo3.java
 * SelectionDemo:   https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/painting/SelectionDemoProject/src/painting/SelectionDemo.java
 */

public class NodeEditor {
    /* Node Editor class */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showInterface();
            }
        });
    }

    private static void showInterface() {
        JFrame f = new JFrame("Node Editor");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new MyPanel());
        f.pack();
        f.setVisible(true);
    }
}

class MyPanel extends JPanel {
    /* Panel parameters */
    private int totalWidth = 600, totalHeight = 300;
    private int separateLine = 150;

    /* Boxes */
    private int INIT_CAPACITY = 10;
    private ArrayList<Box> boxes = new ArrayList<Box>(INIT_CAPACITY);
    private Box selectedBox = null;

    /* Line styles */
    private LineStyle[] lineStyles = LineStyle.defaultLineStyles;
    private LineStyle selectedLineStyle;

    /* User actions */
    private int action;
    private final int DRAW = 0, MOVE = 1;

    /* Action parameters */
    private int startX, startY; // mouse press position
    private int drawX, drawY, drawWidth, drawHeight; // box to draw
    private int baseX, baseY; 	// base position when moving box
    private boolean doneDrawing;

    public MyPanel() {
        // set default style
        setBackground(Color.WHITE);
        selectedLineStyle = lineStyles[0];
        selectedLineStyle.setSelected(true);

        // add mouse listener
        MyMouseInputListener myListener = new MyMouseInputListener();
        addMouseListener(myListener);
        addMouseMotionListener(myListener);

        // add window resize listener
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                totalWidth = e.getComponent().getWidth();
                totalHeight = e.getComponent().getHeight();
                repaint();
            }
        });

        /* JButton usage reference:
         * https://www.tutorialspoint.com/swing/swing_jbutton.htm */

        // add delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedBox != null)
                    selectedBox.setVisible(false);
                repaint();
            }
        });

        setLayout(null);
        deleteButton.setBounds(30, 120, 100, 20);
        add(deleteButton);
    }

    private class MyMouseInputListener extends MouseInputAdapter {
        public void mousePressed(MouseEvent e) {
            /* Mouse pressed event */
            System.out.println("Mouse pressed");
            startX = e.getX();
            startY = e.getY();

            // determine user action: draw or move
            action = DRAW;
            for (int idx = boxes.size() - 1; idx >= 0; --idx) {
                Box box = boxes.get(idx);
                if (box.isClickedBy(e) && box.isSelected()) {
                    action = MOVE;
                    baseX = box.getStartX();
                    baseY = box.getStartY();
                    break;
                }
            }

            // start drawing and deselect any box
            if (action == DRAW) {
                doneDrawing = false;
                updateSelectedBox(null);
            }
        }

        public void mouseDragged(MouseEvent e) {
            /* Mouse dragged event */
            System.out.println("Mouse dragged");

            if (action == DRAW)
                updateNewRectangle(e);
            if (action == MOVE)
                updateOldRectangle(e);
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            /* Mouse released event */
            System.out.println("Mouse released");

            if (action == DRAW) {
                updateNewRectangle(e);
                doneDrawing = true;

                // add the new drawn box to the box list
                String boxName = "Box " + (boxes.size() + 1);
                if (drawWidth > 0 && drawHeight > 0)
                    boxes.add(new Box(drawX, drawY, drawWidth, drawHeight,
                            boxName, selectedLineStyle));
            }
            repaint();
        }

        public void mouseClicked(MouseEvent e) {
            /* Mouse clicked event */
            System.out.println("Mouse clicked");

            // check if any line style is clicked
            for (LineStyle ls: lineStyles) {
                if (ls.isClickedBy(e)) {
                    updateSelectedBox(null);
                    updateSelectedLineStyle(ls);
                }
            }

            // check if any box is clicked
            for (int idx = boxes.size() - 1; idx >= 0; --idx) {
                Box box = boxes.get(idx);
                if (box.isClickedBy(e)) {
                    updateSelectedBox(box);
                    updateSelectedLineStyle(box.getLineStyle());
                    break;
                }
            }
            // repaint is done in MouseReleased
        }

        private void updateSelectedBox(Box box) {
            if (selectedBox != null)
                selectedBox.setSelected(false);
            selectedBox = box;
            if (box != null)
                box.setSelected(true);
        }

        private void updateSelectedLineStyle(LineStyle ls) {
            selectedLineStyle.setSelected(false);
            selectedLineStyle = ls;
            ls.setSelected(true);
        }

        private int clipX(int x, int width) {
            return Math.min(Math.max(x, separateLine), totalWidth - width);
        }

        private int clipY(int y, int height) {
            return Math.min(Math.max(y, 0), totalHeight - height);
        }

        private void updateNewRectangle(MouseEvent e) {
            // find new box coordinates
            int oneX = Math.min(startX, e.getX());
            int oneY = Math.min(startY, e.getY());
            int anotherX = Math.max(startX, e.getX());
            int anotherY = Math.max(startY, e.getY());

            // clip the box within the region
            drawX = clipX(oneX, 0);
            drawY = clipY(oneY, 0);
            drawWidth = clipX(anotherX, 0) - drawX;
            drawHeight = clipY(anotherY, 0) - drawY;
        }

        private void updateOldRectangle(MouseEvent e) {
            // find new coordinates
            int newX = baseX + (e.getX() - startX);
            int newY = baseY + (e.getY() - startY);

            // restrict the box within the region
            selectedBox.setStart(
                clipX(newX, selectedBox.getWidth()),
                clipY(newY, selectedBox.getHeight())
            );
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(totalWidth, totalHeight);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        // draw a separate line 
        g.drawLine(separateLine, 0, separateLine, totalHeight);

        // show all line styles
        for (LineStyle ls: lineStyles)
            ls.display(g);

        // show all boxes
        for (Box box: boxes)
            box.display(g);

        // show the box being drawn
        if (!doneDrawing && drawWidth > 0 && drawHeight > 0) {
            Box drawnBox = new Box(drawX, drawY, drawWidth, drawHeight,
                    null, selectedLineStyle);
            drawnBox.display(g);
        }
    }  
}