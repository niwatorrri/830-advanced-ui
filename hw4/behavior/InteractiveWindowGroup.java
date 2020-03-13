package behavior;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;

import graphics.group.Group;
import graphics.object.BoundaryRectangle;
import graphics.object.GraphicalObject;
import behavior.Behavior;

public class InteractiveWindowGroup extends JFrame implements Group {
    private static final long serialVersionUID = 1L;
    protected Image buffer;
    private JComponent canvas;
    private List<GraphicalObject> children = new ArrayList<>();
    private List<Behavior> behaviors = new ArrayList<>();

    // make a top-level window with specified title, width and height
    public InteractiveWindowGroup(String title, int width, int height) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addMouseListener(new WindowMouseListener());
        this.addKeyListener(new WindowKeyListener());

        // Container content = getContentPane();
        // content.setLayout(new BorderLayout());

        canvas = new JComponent() {
            private static final long serialVersionUID = 1L;
            public void paintComponent(Graphics graphics) {
                if (buffer != null) {
                    graphics.drawImage(buffer, 0, 0, null);
                }
            }
        };
        canvas.setBackground(Color.white);
        canvas.setPreferredSize(new Dimension(width, height));
        // content.add(BorderLayout.CENTER, canvas);

        // message = new JTextArea(10, 40);
        // message.setEditable(false);
        // message.addMouseListener(ml);
        // content.add(BorderLayout.SOUTH, new JScrollPane(message));

        this.pack();
        this.setVisible(true);

        this.makeBuffer(width, height); // must be after setVisible
    }

    private class WindowMouseListener extends MouseAdapter {
        private int handleModifiers(InputEvent event) {
            int modifiers = BehaviorEvent.NO_MODIFIER;
            modifiers |= event.isAltDown() ? BehaviorEvent.ALT_MODIFIER : 0;
            modifiers |= event.isControlDown() ? BehaviorEvent.CONTROL_MODIFIER : 0;
            modifiers |= event.isShiftDown() ? BehaviorEvent.SHIFT_MODIFIER : 0;
            modifiers |= event.isMetaDown() ? BehaviorEvent.COMMAND_KEY_MODIFIER : 0;
            // TODO: check meta and other modifiers
            return modifiers;
        }

        private int handleKey(MouseEvent event, int id) {
            if (BehaviorEvent.isMouseEvent(id)) {
                int button = event.getButton();
                if (button == MouseEvent.BUTTON1) {
                    return BehaviorEvent.LEFT_MOUSE_KEY;
                } else if (button == MouseEvent.BUTTON2) {
                    return BehaviorEvent.MIDDLE_MOUSE_KEY;
                } else if (button == MouseEvent.BUTTON3) {
                    return BehaviorEvent.RIGHT_MOUSE_KEY;
                }
            }
            if (BehaviorEvent.isMouseWheelEvent(id)) {
                int scroll = ((MouseWheelEvent) event).getWheelRotation();
                if (scroll < 0) {
                    return BehaviorEvent.SCROLLWHEEL_UP_KEY;
                } else if (scroll > 0) {
                    return BehaviorEvent.SCROLLWHEEL_DOWN_KEY;
                }
            }
            return BehaviorEvent.NO_KEY;
        }

        public void mousePressed(MouseEvent event) {
            int id = BehaviorEvent.MOUSE_DOWN_ID;
            BehaviorEvent e = new BehaviorEvent(id,
                handleModifiers(event),
                handleKey(event, id),
                event.getX(),
                event.getY()
            );
        }
        public void mouseReleased(MouseEvent event) {
            int id = BehaviorEvent.MOUSE_UP_ID;
            BehaviorEvent e = new BehaviorEvent(id,
                handleModifiers(event),
                handleKey(event, id),
                event.getX(),
                event.getY()
            );
        }
        public void mouseMoved(MouseEvent event) {
            //
        }
        public void mouseWheelMoved(MouseWheelEvent event) {
            int id = BehaviorEvent.SCROLLWHEEL_ID;
            BehaviorEvent e = new BehaviorEvent(id,
                handleModifiers(event),
                handleKey(event, id),
                event.getX(),
                event.getY()
            );
        }
    }

    private class WindowKeyListener extends KeyAdapter {
        //
    }

    private void makeBuffer(int width, int height) {
        buffer = createImage(width, height);
        Graphics2D graphics = (Graphics2D) buffer.getGraphics();
        graphics.setColor(canvas.getBackground());
        graphics.fillRect(0, 0, width, height);
    }
    
    public InteractiveWindowGroup() {
        this("Interactive Window", 400, 400);
    }

    public void redraw(GraphicalObject object) {
        Graphics2D graphics = (Graphics2D) buffer.getGraphics();
        BoundaryRectangle area = new BoundaryRectangle(
                0, 0, getWidth(), getHeight());
        graphics.setColor(canvas.getBackground());
        graphics.fill(area);
        object.draw(graphics, area);
        canvas.repaint();
    }

    /**
     * Methods defined in the Group and GraphicalObject interface
     */
    public void addChild(GraphicalObject child) {
        if (!children.isEmpty()) {
            String message = "top level window only supports one child";
            throw new IllegalArgumentException(message);
        } else {
            children.add(child);
            child.setGroup(this);
            redraw(child);
        }
    }

    public void removeChild(GraphicalObject child) {
        children.remove(child);
        child.setGroup(null);
    }

    public void bringChildToFront(GraphicalObject child) {
    }

    public void resizeToChildren() {
    }

    public List<GraphicalObject> getChildren() {
        return new ArrayList<GraphicalObject>(children);
    }

    public Point parentToChild(Point pt) {
        return pt;
    }

    public Point childToParent(Point pt) {
        return pt;
    }

    public void draw(Graphics2D graphics, Shape clipRect) {
    }

    public BoundaryRectangle getBoundingBox() {
        return (BoundaryRectangle) canvas.getBounds();
    }

    public void moveTo(int x, int y) {
    }

    public Group getGroup() {
        return null;
    }

    public void setGroup(Group group) {
    }

    public boolean contains(int x, int y) {
        return getBoundingBox().contains(x, y);
    }
}