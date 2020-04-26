package ui.talk;

import static com.example.dialogflow.DetectIntentAudio.detectIntentAudio;

import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import ui.toolkit.behavior.InteractiveWindowGroup;
import ui.toolkit.graphics.group.Group;
import ui.toolkit.graphics.group.LayoutGroup;
import ui.toolkit.graphics.group.SimpleGroup;
import ui.toolkit.graphics.object.BoundaryRectangle;
import ui.toolkit.graphics.object.FilledRect;
import ui.toolkit.graphics.object.GraphicalObject;
import ui.toolkit.graphics.object.Line;
import ui.toolkit.graphics.object.Text;
import ui.toolkit.widget.Button;
import ui.toolkit.widget.ButtonPanel;
import ui.toolkit.widget.PropertySheet;
import ui.toolkit.widget.RadioButton;
import ui.toolkit.widget.RadioButtonPanel;
import ui.toolkit.widget.Widget;

// TODO: integrate this into the dialogflow branch's ui folder
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

    private Group controlPlane, drawingPanel, voiceControlPlane;

    private static String sessionId;
    private static String projectId;

    public static void main(String[] args) {
        // parse command line arguments
        String command = args[0];
        if (command.equals("--sessionId")) {
            sessionId = args[1];
        }
        command = args[2];
        if (command.equals("--projectId")) {
            projectId = args[3];
        }

        // start Talk UI interface
        new TalkUI(sessionId, projectId);
    }

    public TalkUI() {

    }

    public TalkUI(String sessionId, String projectId) {
        super("TalkUI Editor", WINDOW_WIDTH, WINDOW_HEIGHT);
        makeGUI();
        startListening();
    }

    private void startListening() {
        // caveat: if your device didn't prompt you to grant permission
        // of your mic to this application, it's probably not going to work
        final AudioRecorder recorder = new AudioRecorder();
        // recorder.record(5);

        QueryResult queryResult = null;
        try {
            String audioFilePath = recorder.getAudioFilePath();
            String languageCode = "en-US";
            queryResult = detectIntentAudio(projectId, audioFilePath, sessionId, languageCode);
        } catch (Exception e) {
            System.err.println("Query failed: " + e);
        }

        String intentName = queryResult.getIntent().getDisplayName();

        if (intentName.equals(Intent.INITIALIZATION)) {
            Struct parameters = queryResult.getParameters();

            Value newObject = parameters.getFieldsOrDefault(Intent.InitializationParams.GRAPHICAL_OBJECT, null);
            if (newObject != null) { // TODO: probably always not null?
                switch (newObject.getStringValue()) {
                    case Entity.GraphicalObjectType.FILLED_RECT: {
                        int width = (int) parameters.getFieldsOrDefault(Intent.InitializationParams.WIDTH, null)
                                .getNumberValue();
                        int height = (int) parameters.getFieldsOrDefault(Intent.InitializationParams.HEIGHT, null)
                                .getNumberValue();
                        String color = parameters.getFieldsOrDefault(Intent.InitializationParams.COLOR, null)
                                .getStringValue();
                        System.out.println(width + " " + height + " " + color);
                        addChild(new FilledRect(20, 20, width, height, Entity.stringToColor.get(color)));
                    }
                }
            }
        }

        String detectedText = queryResult.getQueryText();
        addChild(new Text(detectedText));
    }

    private void makeGUI() {
        // create example widget
        RadioButtonPanel radioPanel = new RadioButtonPanel(50, 50)
                .addChildren(new RadioButton(new Line(0, 10, 40, 10, Color.BLACK, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.BLUE, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.MAGENTA, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.CYAN, 3)))
                .setSelection("one");

        // setup groups and separation line
        Line separationLine = new Line(SEPARATION_LEFT, 0, SEPARATION_LEFT, WINDOW_HEIGHT, Color.BLACK, 2);

        voiceControlPlane = new LayoutGroup(0, 0, CONTROL_PLANE_WIDTH, VOICE_PLANE_HEIGHT, LayoutGroup.VERTICAL, 20);
        // add plane texts
        Text voicePlaneText = new Text("Voice Control Plane");
        Widget<?> exportButton = new ButtonPanel(0, 0, false, ButtonPanel.MULTIPLE).addChild(new Button("Save"))
                .setCallback(o -> {
                    try {
                        if (drawingPanel == null)
                            return;
                        BoundaryRectangle drawingBox = drawingPanel.getBoundingBox();
                        // retrieve image
                        BufferedImage bi = getBufferedImage().getSubimage(drawingBox.x, drawingBox.y,
                                drawingBox.width, drawingBox.height);

                        File outputFile = new File("saved.png");
                        ImageIO.write(bi, "png", outputFile);
                        System.out.println("Canvas saved!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        voiceControlPlane.addChildren(voicePlaneText, exportButton);

        // the (x, y) does not matter since the control plane is a LayoutGroup
        // TODO: should use radioPanel.getValue() to get active value, but somehow the
        // active value after init is null, though in the UI first radio button selected
        PropertySheet propertySheet = new PropertySheet(radioPanel.getChildren().get(0), this);
        radioPanel.setCallback(o -> {
            for (GraphicalObject child : radioPanel.getChildren()) {
                if (((RadioButton) child).isSelected()) {
                    System.out.println("update selection...");
                    propertySheet.updatePropertySheet(child);
                }
            }
        });

        JComponent propertyControlPlane = new JScrollPane(propertySheet);
        propertyControlPlane.setBounds(BORDER_GAP, (CONTROL_PLANE_HEIGHT) / 2 + BORDER_GAP, CONTROL_PLANE_WIDTH,
                PROPERTY_PLANE_HEIGHT);
        getCanvas().add(propertyControlPlane);

        // set the offset to BORDER_GAP
        controlPlane = new LayoutGroup(BORDER_GAP, BORDER_GAP, CONTROL_PLANE_WIDTH, CONTROL_PLANE_HEIGHT,
                LayoutGroup.VERTICAL, BORDER_GAP).addChildren(voiceControlPlane);

        drawingPanel = new SimpleGroup(SEPARATION_LEFT, 0, SEPARATION_RIGHT, WINDOW_HEIGHT);

        drawingPanel.addChild(radioPanel);

        addChildren(controlPlane, separationLine, drawingPanel);
    }
}