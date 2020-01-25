package nodeeditor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.*;

/* 
 * Starter code references: Java Swing Tutorial written by Oracle
 * SwingPaintDemo3: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/painting/SwingPaintDemo3Project/src/painting/SwingPaintDemo3.java
 * SelectionDemo: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/painting/SelectionDemoProject/src/painting/SelectionDemo.java
 */

public class NodeEditor {
    /* NodeEditor class */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showInterface();
            }
        });
    }

    private static void showInterface() {
        JFrame frame = new JFrame("Node Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MyPanel());
        frame.pack();
        frame.setVisible(true);
    }
}

class MyPanel extends JPanel {
    /* MyPanel class */

    /* Panel parameters */
    private int totalWidth = 800, totalHeight = 400;
    private int separateLine = 150;

    /* Boxes */
    private int INIT_CAPACITY = 10;
    private ArrayList<Box> boxes = new ArrayList<Box>(INIT_CAPACITY);
    private Box selectedBox = null;

    /* Line styles */
    private LineStyle[] lineStyles = LineStyle.defaultLineStyles;
    private LineStyle selectedLineStyle;

    /* Colors */
    private Color selectedLineColor = Color.BLACK;
    private Color selectedFaceColor = Color.WHITE;

    /* Cursor shapes */
    private Cursor DEFAULT_CURSOR = Cursor.getDefaultCursor();
    private Cursor MOVE_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    /* User actions */
    private int action;
    private final int DRAW = 0, MOVE = 1;

    /* Action parameters */
    private int startX, startY; // mouse press position
    private int drawX, drawY, drawWidth, drawHeight; // box to draw
    private int baseX, baseY; 	// base position when moving box
    private boolean doneDrawing;

    public MyPanel() {
        /* MyPanel constructor */
        // set default line style
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

        // support deletion with backspace key
        // Reference: https://www.decodejava.com/java-keylistener.htm
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && selectedBox != null)
                    selectedBox.setVisible(false);
                repaint();
            }
        });

        /* JColorChooser usage reference:
         * http://www.java2s.com/Code/Java/Swing-JFC/ColorChooserSample1.htm */

        // add button to choose line color
        JButton lineColorChooserButton = new JButton("LineColor");
        lineColorChooserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newLineColor = JColorChooser.showDialog(null,
                    "Choose a line color", selectedLineColor);
                if (newLineColor != null)
                    selectedLineColor = newLineColor;
                repaint();
            }
        });
        
        // add button to choose face color
        JButton faceColorChooserButton = new JButton("FaceColor");
        faceColorChooserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newFaceColor = JColorChooser.showDialog(null,
                    "Choose a face color", selectedFaceColor);
                if (newFaceColor != null)
                    selectedFaceColor = newFaceColor;
                repaint();
            }
        });

        // add button to bring box to top
        JButton bringToTopButton = new JButton("BringToTop");
        bringToTopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedBox != null) {
                    boxes.remove(selectedBox);
                    boxes.add(selectedBox);
                    repaint();
                }
            }
        });

        // set button size and position
        int buttonWidth = 100, buttonHeight = 25;
        int buttonX = (separateLine - buttonWidth) / 2;

        setLayout(null);
        deleteButton.setBounds(buttonX, 210, buttonWidth, buttonHeight);
        lineColorChooserButton.setBounds(buttonX, 240, buttonWidth, buttonHeight);
        faceColorChooserButton.setBounds(buttonX, 270, buttonWidth, buttonHeight);
        bringToTopButton.setBounds(buttonX, 300, buttonWidth, buttonHeight);

        add(deleteButton);
        add(lineColorChooserButton);
        add(faceColorChooserButton);
        add(bringToTopButton);
    }

    private class MyMouseInputListener extends MouseInputAdapter {
        /* Callbacks for mouse events */
        public void mousePressed(MouseEvent e) {
            /* Mouse pressed event */
            startX = e.getX();
            startY = e.getY();

            // determine user action: draw or move
            action = DRAW;
            for (int idx = boxes.size() - 1; idx >= 0; --idx) {
                Box box = boxes.get(idx);
                if (box.isInvolvedIn(e)) {
                    if (box.isSelected()) {
                        action = MOVE;
                        baseX = box.getStartX();
                        baseY = box.getStartY();
                    }
                    break;
                }
            }

            // if draw, start drawing and deselect any box
            if (action == DRAW) {
                doneDrawing = false;
                updateSelectedBox(null);
            }
        }

        public void mouseDragged(MouseEvent e) {
            /* Mouse dragged event */
            if (action == DRAW)
                updateRectangleForDraw(e);
            if (action == MOVE)
                updateRectangleForMove(e);
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            /* Mouse released event */
            if (action == DRAW) {
                updateRectangleForDraw(e);
                doneDrawing = true;

                // add the new drawn box to the box list
                String boxName = "Box " + (boxes.size() + 1);
                if (drawWidth > 0 && drawHeight > 0) {
                    boxes.add(new Box(drawX, drawY, drawWidth, drawHeight, boxName,
                            selectedLineStyle, selectedLineColor, selectedFaceColor));
                }
                repaint();
            }
        }

        public void mouseClicked(MouseEvent e) {
            /* Mouse clicked event */
            // check if any line style is clicked
            for (LineStyle ls: lineStyles) {
                if (ls.isInvolvedIn(e)) {
                    updateSelectedBox(null);
                    updateSelectedLineStyle(ls);
                }
            }

            // check if any box is clicked
            for (int idx = boxes.size() - 1; idx >= 0; --idx) {
                Box box = boxes.get(idx);
                if (box.isInvolvedIn(e)) {
                    updateSelectedBox(box);
                    updateSelectedLineStyle(box.getLineStyle());
                    setCursor(MOVE_CURSOR);
                    break;
                }
            }

            // repaint the selected item if any
            repaint();
        }

        public void mouseMoved(MouseEvent e) {
            /* Mouse moved event */
            // determine cursor shape: default or move
            for (int idx = boxes.size() - 1; idx >= 0; --idx) {
                Box box = boxes.get(idx);
                if (box.isInvolvedIn(e)) {
                    if (box.isSelected())
                        setCursor(MOVE_CURSOR);
                    return;
                }
            }
            setCursor(DEFAULT_CURSOR);
        }

        private void updateSelectedBox(Box box) {
            // deselect the old box and select the new box
            if (selectedBox != null)
                selectedBox.setSelected(false);
            selectedBox = box;
            if (box != null)
                box.setSelected(true);
        }

        private void updateSelectedLineStyle(LineStyle ls) {
            // deselect the old linestyle and select the new linestyle
            selectedLineStyle.setSelected(false);
            selectedLineStyle = ls;
            ls.setSelected(true);
        }

        private int clipX(int x, int width) {
            // keep the box within a proper X range
            return Math.min(Math.max(x, separateLine), totalWidth - width);
        }

        private int clipY(int y, int height) {
            // keep the box within a proper Y range
            return Math.min(Math.max(y, 0), totalHeight - height);
        }

        private void updateRectangleForDraw(MouseEvent e) {
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

        private void updateRectangleForMove(MouseEvent e) {
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
        // set preferred window size
        return new Dimension(totalWidth, totalHeight);
    }

    private void showSelectedColors(Graphics g) {
        // show the selected line color and face color
        int COLOR_WIDTH = 15;
        g.setColor(selectedLineColor);
        g.fillRect(125, 245, COLOR_WIDTH, COLOR_WIDTH);
        g.setColor(selectedFaceColor);
        g.fillRect(125, 275, COLOR_WIDTH, COLOR_WIDTH);
        g.setColor(Color.BLACK);
        g.drawRect(125, 245, COLOR_WIDTH, COLOR_WIDTH);
        g.drawRect(125, 275, COLOR_WIDTH, COLOR_WIDTH);
    }

    public void paintComponent(Graphics g) {
        /* Paint all the components on the panel */
        super.paintComponent(g);
        requestFocusInWindow(); // listen to keyboard input

        // draw a separate line 
        g.drawLine(separateLine, 0, separateLine, totalHeight);

        // show current colors
        showSelectedColors(g);

        // show all line styles
        for (LineStyle ls: lineStyles)
            ls.display(g);

        // show all boxes
        for (Box box: boxes)
            box.display(g);

        // show the box being drawn if any
        if (!doneDrawing && drawWidth > 0 && drawHeight > 0) {
            Box drawnBox = new Box(drawX, drawY, drawWidth, drawHeight, null,
                    selectedLineStyle, selectedLineColor, selectedFaceColor);
            drawnBox.display(g);
        }
    }  
}