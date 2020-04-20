package ui.editor;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import ui.toolkit.behavior.InteractiveWindowGroup;
import ui.toolkit.graphics.group.Group;
import ui.toolkit.graphics.group.LayoutGroup;
import ui.toolkit.graphics.group.SimpleGroup;
import ui.toolkit.graphics.object.Line;
import ui.toolkit.graphics.object.Text;
import ui.toolkit.widget.PropertySheet;
import ui.toolkit.widget.RadioButton;
import ui.toolkit.widget.RadioButtonPanel;

public class TalkUI extends InteractiveWindowGroup {
    private static final long serialVersionUID = 1L;

    private static final int BORDER_GAP = 10;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int SEPARATION_LEFT = 200;
    private static final int SEPARATION_RIGHT = WINDOW_WIDTH - SEPARATION_LEFT;
    private static final int CONTROL_PLANE_WIDTH = SEPARATION_LEFT - BORDER_GAP * 2;;
    private static final int CONTROL_PLANE_HEIGHT = WINDOW_HEIGHT - BORDER_GAP * 2;
    private static final int VOICE_PLANE_HEIGHT = (CONTROL_PLANE_HEIGHT) / 2 - BORDER_GAP;
    private static final int PROPERTY_PLANE_HEIGHT = CONTROL_PLANE_HEIGHT - VOICE_PLANE_HEIGHT - BORDER_GAP;

    public static void main(String[] args) {
        new TalkUI();
    }

    public TalkUI() {
        super("TalkUI Editor", WINDOW_WIDTH, WINDOW_HEIGHT);

        // create example widget
        RadioButtonPanel radioPanel = new RadioButtonPanel(50, 50)
                .addChildren(new RadioButton(new Line(0, 10, 40, 10, Color.BLACK, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.BLUE, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.MAGENTA, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.CYAN, 3)))
                .setSelection("one");

        // setup groups and separation line
        Line separationLine = new Line(SEPARATION_LEFT, 0, SEPARATION_LEFT, WINDOW_HEIGHT, Color.BLACK, 2);

        Group voiceControlPlane = new LayoutGroup(0, 0, CONTROL_PLANE_WIDTH, VOICE_PLANE_HEIGHT, LayoutGroup.VERTICAL,
                20);
        // add plane texts
        Text voicePlaneText = new Text("Voice Control Plane");
        voiceControlPlane.addChild(voicePlaneText);

        // the (x, y) does not matter since the control plane is a LayoutGroup
        // TODO: should use radioPanel.getValue() to get active value, but somehow the
        // active value after init is null, though in the UI first radio button selected
        PropertySheet psheet = new PropertySheet(radioPanel.getChildren().get(0), this);
        JComponent propertyControlPlane = new JScrollPane(psheet);
        propertyControlPlane.setBounds(BORDER_GAP, (CONTROL_PLANE_HEIGHT) / 2 + BORDER_GAP, CONTROL_PLANE_WIDTH,
                PROPERTY_PLANE_HEIGHT);
        getCanvas().add(propertyControlPlane);

        // TODO: the property sheet should be updated with the active value, may need
        // coordination with voice control part (e.g. the newly created graphical object
        // is the active and we set the property sheet with it)

        // set the offset to BORDER_GAP
        Group controlPlane = new LayoutGroup(BORDER_GAP, BORDER_GAP, CONTROL_PLANE_WIDTH, CONTROL_PLANE_HEIGHT,
                LayoutGroup.VERTICAL, BORDER_GAP).addChildren(voiceControlPlane);

        Group drawingPanel = new SimpleGroup(SEPARATION_LEFT, 0, SEPARATION_RIGHT, WINDOW_HEIGHT);

        drawingPanel.addChild(radioPanel);

        addChildren(controlPlane, separationLine, drawingPanel);

    }
}