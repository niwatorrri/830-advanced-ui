package behavior;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;

import graphics.group.Group;
import graphics.object.BoundaryRectangle;
import graphics.object.GraphicalObject;

public class InteractiveWindowGroup extends JFrame implements Group {
    private static final long serialVersionUID = 1L;

    protected BufferedImage buffer;
    private JComponent canvas;
    private Insets insets;

    private List<GraphicalObject> children = new ArrayList<>();
    private List<Behavior> behaviors = new ArrayList<>();

    /**
     * Constructor: make a top-level window with specified title, width and height
     */
    public InteractiveWindowGroup(String title, int width, int height) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        WindowMouseListener mouseListener = new WindowMouseListener();
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        this.addMouseWheelListener(mouseListener);
        this.addKeyListener(new WindowKeyListener());

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

        this.add(canvas);
        this.pack();
        this.setVisible(true);
        this.makeBuffer(width, height);
        this.insets = getInsets();
    }

    private void handleBehaviorEvent(BehaviorEvent behaviorEvent) {
        for (Behavior behavior : behaviors) {
            behavior.check(behaviorEvent);
        }
        if (!children.isEmpty()) {
            redraw(children.get(0));
        }
    }

    // Mouse listener
    private class WindowMouseListener extends MouseAdapter {
        public void mousePressed(MouseEvent event) {
            int id = BehaviorEvent.MOUSE_DOWN_ID;
            handleBehaviorEvent(getBehaviorEvent(event, id));
        }

        public void mouseReleased(MouseEvent event) {
            int id = BehaviorEvent.MOUSE_UP_ID;
            handleBehaviorEvent(getBehaviorEvent(event, id));
        }

        public void mouseMoved(MouseEvent event) {
            int id = BehaviorEvent.MOUSE_MOVE_ID;
            handleBehaviorEvent(getBehaviorEvent(event, id));
        }

        public void mouseDragged(MouseEvent event) {
            int id = BehaviorEvent.MOUSE_DRAG_ID;
            handleBehaviorEvent(getBehaviorEvent(event, id));
        }

        public void mouseClicked(MouseEvent event) {
            int id = BehaviorEvent.MOUSE_CLICK_ID;
            handleBehaviorEvent(getBehaviorEvent(event, id));
        }

        public void mouseWheelMoved(MouseWheelEvent event) {
            int id = BehaviorEvent.SCROLLWHEEL_ID;
            handleBehaviorEvent(getBehaviorEvent(event, id));
        }

        // Convert an awt MouseEvent to our BehaviorEvent
        private BehaviorEvent getBehaviorEvent(MouseEvent event, int id) {
            return new BehaviorEvent(
                getModifiers(event),
                getKey(event, id), id,
                event.getX() - insets.left,
                event.getY() - insets.top - 1  // at least this works on MacOS
            );
        }

        // Get mouse event keys
        private int getKey(MouseEvent event, int id) {
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
    }

    // Get modifiers: applicable to all input events
    private int getModifiers(InputEvent event) {
        int modifiers = BehaviorEvent.NO_MODIFIER;
        modifiers |= event.isAltDown() ? BehaviorEvent.ALT_MODIFIER : 0;
        modifiers |= event.isControlDown() ? BehaviorEvent.CONTROL_MODIFIER : 0;
        modifiers |= event.isShiftDown() ? BehaviorEvent.SHIFT_MODIFIER : 0;
        modifiers |= event.isMetaDown() ? BehaviorEvent.COMMAND_MODIFIER : 0;
        return modifiers;
    }

    // Key listener
    private class WindowKeyListener extends KeyAdapter {
        private Point getCursor() {
            Point cursor = getMousePosition();
            return (cursor != null) ? cursor : new Point(0, 0);
        }

        public void keyPressed(KeyEvent event) {
            Point cursor = getCursor();
            handleBehaviorEvent(new BehaviorEvent(
                getModifiers(event),
                event.getKeyCode(),
                BehaviorEvent.KEY_DOWN_ID,
                cursor.x - insets.left,
                cursor.y - insets.top
            ));
        }

        public void keyReleased(KeyEvent event) {
            Point cursor = getCursor();
            handleBehaviorEvent(new BehaviorEvent(
                getModifiers(event),
                event.getKeyCode(),
                BehaviorEvent.KEY_UP_ID,
                cursor.x - insets.left,
                cursor.y - insets.top
            ));
        }
    }

    private void makeBuffer(int width, int height) {
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = buffer.createGraphics();
        graphics.setColor(canvas.getBackground());
        graphics.fillRect(0, 0, width, height);
        graphics.dispose();
    }
    
    public InteractiveWindowGroup() {
        this("Interactive Window", 400, 400);
    }

    public void redraw(GraphicalObject object) {
        Graphics2D graphics = buffer.createGraphics();
        BoundaryRectangle r = new BoundaryRectangle(0, 0, getWidth(), getHeight());
        graphics.setColor(canvas.getBackground());
        graphics.fill(r);
        object.draw(graphics, r);
        graphics.dispose();
        canvas.repaint();
    }

    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
    }

    public void removeBehavior(Behavior behavior) {
        behaviors.remove(behavior);
    }

    /**
     * Methods defined in the Group and GraphicalObject interfaces
     */
    public Group addChild(GraphicalObject child) {
        if (!children.isEmpty()) {
            String message = "top level window only supports one child";
            throw new IllegalArgumentException(message);
        } else {
            children.add(child);
            child.setGroup(this);
            redraw(child);
        }
        return this;
    }

    public Group removeChild(GraphicalObject child) {
        children.remove(child);
        child.setGroup(null);
        return this;
    }

    public Group bringChildToFront(GraphicalObject child) {
        return this;
    }

    public Group resizeToChildren() {
        return this;
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
        return new BoundaryRectangle(canvas.getBounds());
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