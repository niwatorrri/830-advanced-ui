package ui.talk;

import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import ui.toolkit.graphics.group.Group;
import ui.toolkit.graphics.object.FilledRect;
import ui.toolkit.graphics.object.Text;


class HandleResponse {
    public static void handle(QueryResult queryResult, Group panel) {
        String intentName = queryResult.getIntent().getDisplayName();

        if (intentName.equals(Intent.INITIALIZATION)) {
            Struct params = queryResult.getParameters();

            Value newObject = params.getFieldsOrDefault(Intent.InitializationParams.GRAPHICAL_OBJECT, null);
            if (newObject != null) { // TODO: probably always not null?
                switch (newObject.getStringValue()) {
                    case Entity.GraphicalObjectType.FILLED_RECT: {
                        int width = (int) params.getFieldsOrDefault(Intent.InitializationParams.WIDTH, null)
                                .getNumberValue();
                        int height = (int) params.getFieldsOrDefault(Intent.InitializationParams.HEIGHT, null)
                                .getNumberValue();
                        String color = params.getFieldsOrDefault(Intent.InitializationParams.COLOR, null)
                                .getStringValue();
                        System.out.println(width + " " + height + " " + color);
                        panel.addChild(new FilledRect(20, 20, width, height, Entity.stringToColor.get(color)));
                    }
                }
            }
        }

        String detectedText = queryResult.getQueryText();
        panel.addChild(new Text(detectedText));
    }
}