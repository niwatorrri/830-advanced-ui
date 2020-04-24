package ui.talk;

import static com.example.dialogflow.DetectIntentAudio.detectIntentAudio;

import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import ui.toolkit.behavior.InteractiveWindowGroup;
import ui.toolkit.graphics.object.FilledRect;
import ui.toolkit.graphics.object.Text;

public class TalkUI extends InteractiveWindowGroup {
    /**
     * Talk UI: interface
     */
    private static final long serialVersionUID = 1L;

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 400;

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

    public TalkUI(String sessionId, String projectId) {
        super("Talk UI", WINDOW_WIDTH, WINDOW_HEIGHT);
        addChild(new Text("Talk UI", WINDOW_WIDTH / 2, 20));

        // caveat: if your device didn't prompt you to grant permission
        // of your mic to this application, it's probably not going to work
        final AudioRecorder recorder = new AudioRecorder();
        // recorder.record(5);

        QueryResult queryResult = null;
        try {
            String audioFilePath = recorder.getAudioFilePath();
            String languageCode = "en-US";
            queryResult = detectIntentAudio(
                projectId, audioFilePath, sessionId, languageCode);
        } catch (Exception e) {
            System.err.println("Query failed: " + e);
        }

        String intentName = queryResult.getIntent().getDisplayName();

        if (intentName.equals(Intent.INITIALIZATION)) {
            Struct parameters = queryResult.getParameters();
            System.out.println(parameters.getFieldsMap());

            Value newObject = parameters.getFieldsOrDefault(Intent.InitializationParams.GRAPHICAL_OBJECT, null);
            System.out.println(newObject);
            System.out.println(newObject.getStringValue());
            System.out.println(Entity.GraphicalObjectType.FILLED_RECT);
            if (newObject != null) {
                switch (newObject.getStringValue()) {
                    case Entity.GraphicalObjectType.FILLED_RECT: {
                        int width = (int) parameters.getFieldsOrDefault(Intent.InitializationParams.WIDTH, null).getNumberValue();
                        int height = (int) parameters.getFieldsOrDefault(Intent.InitializationParams.HEIGHT, null).getNumberValue();
                        String color = parameters.getFieldsOrDefault(Intent.InitializationParams.COLOR, null).getStringValue();
                        System.out.println(width + " " + height + " " + color);
                        addChild(new FilledRect(20, 20, width, height, Entity.stringToColor.get(color)));
                    }
                }
            }
        }

        String detectedText = queryResult.getQueryText();
        addChild(new Text(detectedText));
    }
}