package ui.talk;

import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.StringValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import ui.toolkit.graphics.group.Group;
import ui.toolkit.graphics.object.FilledEllipse;
import ui.toolkit.graphics.object.FilledRect;
import ui.toolkit.graphics.object.Text;
import ui.toolkit.widget.RadioButtonPanel;

import java.util.List;


class HandleResponse {
    public static void handle(QueryResult queryResult, Group panel) {
        String intentName = queryResult.getIntent().getDisplayName();

        if (intentName.equals(Intent.INITIALIZATION)) {
            Struct params = queryResult.getParameters();

            Value newObject = params.getFieldsOrDefault(Intent.InitializationParams.GRAPHICAL_OBJECT, null);
            if (newObject != null) { // DialogFlow will ensure that this param is provided
                switch (newObject.getStringValue()) {

                    case Entity.GraphicalObjectType.FILLED_RECT: {
                        int width = (int) params.getFieldsOrDefault(Intent.InitializationParams.WIDTH, null)
                                .getNumberValue();
                        int height = (int) params.getFieldsOrDefault(Intent.InitializationParams.HEIGHT, null)
                                .getNumberValue();
                        String color = params.getFieldsOrDefault(Intent.InitializationParams.COLOR, null)
                                .getStringValue();
                        System.out.println("Drawing FilledRect: " + width + " " + height + " " + color);
                        panel.addChild(new FilledRect(20, 20, width, height, Entity.stringToColor.get(color)));
                        break;
                    }

                    case Entity.GraphicalObjectType.FILLED_ELLIPSE: {
                        int width = (int) params.getFieldsOrDefault(Intent.InitializationParams.WIDTH, null)
                                .getNumberValue();
                        int height = (int) params.getFieldsOrDefault(Intent.InitializationParams.HEIGHT, null)
                                .getNumberValue();
                        String color = params.getFieldsOrDefault(Intent.InitializationParams.COLOR, null)
                                .getStringValue();
                        System.out.println("Drawing FilledEllipse: " + width + " " + height + " " + color);
                        panel.addChild(new FilledEllipse(20, 20, width, height, Entity.stringToColor.get(color)));
                        break;
                    }

                    case Entity.GraphicalObjectType.TEXT: {
                        String text = params.getFieldsOrDefault(Intent.InitializationParams.TEXT, null)
                                .getStringValue();
                        String color = params.getFieldsOrDefault(Intent.InitializationParams.COLOR, null)
                                .getStringValue();
                        System.out.println("Drawing Text: " + text + " " + color);
                        panel.addChild(new Text(text, 20,20, Text.DEFAULT_FONT, Entity.stringToColor.get(color)));
                        break;
                    }

                    case Entity.GraphicalObjectType.RADIOBUTTON_PANEL: {
                        List<Value> values = params.getFieldsOrDefault(Intent.InitializationParams.OPTIONS, null)
                                .getListValue().getValuesList();
                        String color = params.getFieldsOrDefault(Intent.InitializationParams.COLOR, null)
                                .getStringValue();

                        System.out.println("Drawing RadioButton: " + values.size());
                        RadioButtonPanel radios = new RadioButtonPanel(20, 20);

                        for (Value v: values) {
                            String text = params.getFieldsOrDefault(Intent.InitializationParams.TEXT, null)
                                    .getStringValue();
                            radios.addChild(new Text(text, 0,0, Text.DEFAULT_FONT, Entity.stringToColor.get(color)));
                        }

                        panel.addChild(radios);
                        break;
                    }
                }
            }
        } else if (intentName.equals((Intent.INTERACTION))){

        }

        // all other intents don't need anything to be done

        String detectedText = queryResult.getQueryText();
        panel.addChild(new Text(detectedText));
    }
}