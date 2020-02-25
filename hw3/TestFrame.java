import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import java.lang.reflect.Field;

import javax.swing.*;
import javax.imageio.*;

public class TestFrame extends JFrame implements Group {
    private static final long serialVersionUID = 1L;
    protected Image buffer;
    private JComponent canvas;
    private JTextArea message;

    // make a top-level window with specified title, width and height
    public TestFrame(String title, int width, int height) {
        super(title);
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                unpause();
            }
        };
        addMouseListener(ml);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });

        Container content = getContentPane();
        content.setLayout(new BorderLayout());

        canvas = new JComponent() {
            private static final long serialVersionUID = 1L;

            public void paintComponent(Graphics g) {
                if (buffer != null)
                    g.drawImage(buffer, 0, 0, null);
            }
        };
        canvas.setBackground(Color.white);
        canvas.setPreferredSize(new Dimension(width, height));
        content.add(BorderLayout.CENTER, canvas);

        message = new JTextArea(10, 40);
        message.setEditable(false);
        message.addMouseListener(ml);
        content.add(BorderLayout.SOUTH, new JScrollPane(message));

        pack();
        setVisible(true); // formerly show()

        makeBuffer(width, height); // must be after setVisible
    }

    BoundaryRectangle savedClipRect = null;

    //
    // Drawing GraphicalObjects in the window.
    //

    public void redraw(GraphicalObject gobj) {
        Graphics2D g = (Graphics2D) buffer.getGraphics();
        BoundaryRectangle r = new BoundaryRectangle(0,0, getWidth(), getHeight());
        g.setColor(canvas.getBackground());
        g.fill(r);
        gobj.draw(g, r);
        canvas.repaint();
    }

    private void makeBuffer(int width, int height) {
        buffer = createImage(width, height);
        Graphics2D g = (Graphics2D) buffer.getGraphics();
        g.setColor(canvas.getBackground());
        g.fillRect(0, 0, width, height);
    }

    //
    // Group interface
    //

    // This class is NOT a correct implementation of Group.
    // It's just a test harness that displays GraphicalObjects
    // in a window. It should only have one child, which is a group
    java.util.ArrayList<GraphicalObject> children = new java.util.ArrayList<GraphicalObject>();

    public void addChild(GraphicalObject child) {
        if (children.isEmpty()) { //only one child allowed
            child.setGroup(this);
            redraw(child);
            children.add(child);
        }
        else throw new IllegalArgumentException(
                "top level TestFrame only supports one child");
    }

    public void removeChild(GraphicalObject child) {
    }

    public void bringChildToFront(GraphicalObject child) {
    }

    public void resizeToChildren() {
    }

    public void draw(Graphics2D graphics, Shape clipRect) {
    }

    public BoundaryRectangle getBoundingBox() {
        return (BoundaryRectangle)canvas.getBounds();
    }

    public void moveTo(int x, int y) {
    }

    public Group getGroup() {
        return null;
    }

    public void setGroup(Group group) {
    }

    public java.util.List<GraphicalObject> getChildren() {
        return new java.util.ArrayList<GraphicalObject>(children);
    }

    public Point parentToChild(Point pt) {
        return pt;
    }

    public Point childToParent(Point pt) {
        return pt;
    }

    // @SuppressWarnings("unchecked")
    // public <T> T get(String fieldName) 
    //     throws NoSuchFieldException, IllegalAccessException {
    //     Field field = this.getClass().getDeclaredField(fieldName);
    //     field.setAccessible(true);
    //     return (T) field.get(this);
    // }

    // 
    // Message output
    //

    public void print(Object msg) {
        message.append(msg.toString());
        message.setCaretPosition(message.getDocument().getLength());
    }

    public void println(Object msg) {
        print(msg + "\n");
    }

    // 
    // Sleeping
    //

    public void sleep(int msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
        }
    }

    //
    // Waiting for mouse clicks
    //

    public void pause() {
        println("click to continue...");
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public void unpause() {
        synchronized (this) {
            notify();
        }
    }

    //
    // Random selections
    //

    private static java.util.Random r = new java.util.Random();

    public int random(int n) {
        return r.nextInt(n);
    }

    public Object random(Object[] things) {
        return things[random(things.length)];
    }

    //
    // Loading images from disk
    // Guarantees that the image is loaded, so you don't have to pass
    // an ImageObserver when you call getWidth(), getHeight(), or
    // drawImage().
    // 

    //These were used by the old way to do this:
    //private MediaTracker tracker = new MediaTracker(new Label(""));
    //private int nextID = 0;

    public Image loadImageFully(String filename) throws IOException {
      /*  from  http://www.exampledepot.com/egs/javax.imageio/BasicImageRead.html */
        File file = new File(filename);
        Image image = ImageIO.read(file);
        return image;
    }

}
