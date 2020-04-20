package ui.editor;

import java.awt.Color;

import ui.toolkit.behavior.BehaviorEvent;
import ui.toolkit.behavior.ChoiceBehavior;
import ui.toolkit.behavior.InteractiveWindowGroup;
import ui.toolkit.behavior.MoveBehavior;
import ui.toolkit.constraint.Constraint;
import ui.toolkit.constraint.SetupConstraint;
import ui.toolkit.graphics.group.Group;
import ui.toolkit.graphics.group.LayoutGroup;
import ui.toolkit.graphics.group.SimpleGroup;
import ui.toolkit.graphics.object.GraphicalObject;
import ui.toolkit.graphics.object.Line;
import ui.toolkit.graphics.object.Text;
import ui.toolkit.widget.Button;
import ui.toolkit.widget.ButtonPanel;
import ui.toolkit.widget.CheckBox;
import ui.toolkit.widget.CheckBoxPanel;
import ui.toolkit.widget.NumberSlider;
import ui.toolkit.widget.RadioButton;
import ui.toolkit.widget.RadioButtonPanel;
import ui.toolkit.widget.Widget;

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

        // setup groups and separation line
        Line separationLine = new Line(SEPARATION_LEFT, 0, SEPARATION_LEFT, WINDOW_HEIGHT, Color.BLACK, 2);

        Group voiceControlPlane = new LayoutGroup(0, 0, CONTROL_PLANE_WIDTH, VOICE_PLANE_HEIGHT, LayoutGroup.VERTICAL,
                20);
        // the (x, y) does not matter since the control plane is a LayoutGroup
        Group propertyControlPlane = new LayoutGroup(0, VOICE_PLANE_HEIGHT, CONTROL_PLANE_WIDTH, PROPERTY_PLANE_HEIGHT,
                LayoutGroup.VERTICAL, 20);
        // set the offset to BORDER_GAP
        Group controlPlane = new LayoutGroup(BORDER_GAP, BORDER_GAP, CONTROL_PLANE_WIDTH, CONTROL_PLANE_HEIGHT,
                LayoutGroup.VERTICAL, BORDER_GAP).addChildren(voiceControlPlane, propertyControlPlane);

        Group drawingPanel = new SimpleGroup(SEPARATION_LEFT, 0, SEPARATION_RIGHT, WINDOW_HEIGHT);

        // add example widget
        RadioButtonPanel radioPanel = new RadioButtonPanel(50, 50)
                .addChildren(new RadioButton(new Line(0, 10, 40, 10, Color.BLACK, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.BLUE, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.MAGENTA, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.CYAN, 3)))
                .setSelection("one");

        drawingPanel.addChild(radioPanel);

        // add plane texts
        Text voicePlaneText = new Text("Voice Control Plane");
        Text propertyPlaneText = new Text("Property Change Plane");

        voiceControlPlane.addChild(voicePlaneText);
        propertyControlPlane.addChild(propertyPlaneText);

        addChildren(controlPlane, separationLine, drawingPanel);
    }
}