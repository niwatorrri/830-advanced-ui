package ui.talk;

import static com.example.dialogflow.DetectIntentAudio.detectIntentAudio;

import com.google.cloud.dialogflow.v2.QueryResult;

import ui.toolkit.behavior.InteractiveWindowGroup;
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
        // final AudioRecorder recorder = new AudioRecorder();
        // recorder.record(4);

        QueryResult queryResult = null;
        try {
            String languageCode = "en-US";
            // queryResult = detectIntentAudio(
            //     projectId, recorder.audioFilePath, sessionId, languageCode);
            queryResult = detectIntentAudio(
                projectId, "resources/book_a_room.wav", sessionId, languageCode);
        } catch (Exception e) {
            System.err.println("Query failed: " + e);
        }

        String detectedText = queryResult.getQueryText();
        addChild(new Text(detectedText));
    }
}