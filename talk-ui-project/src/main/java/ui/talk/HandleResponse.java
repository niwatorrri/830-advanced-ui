package ui.talk;

import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.List;

import ui.toolkit.graphics.group.Group;
import ui.toolkit.graphics.object.FilledRect;
import ui.toolkit.widget.RadioButton;
import ui.toolkit.widget.RadioButtonPanel;

class HandleResponse {
    public static void handle(QueryResult queryResult, Group drawingPanel) {
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
                        System.out.println("Extracted params: " + width + " " + height + " " + color);
                        drawingPanel.addChild(new FilledRect(20, 20, width, height, Entity.stringToColor.get(color)));
                    }
                    case Entity.GraphicalObjectType.RADIO_BUTTON_PANEL: {
                        List<Value> options = params.getFieldsOrDefault(Intent.InitializationParams.OPTIONS, null)
                                .getListValue().getValuesList();
                        RadioButtonPanel panel = new RadioButtonPanel();
                        for (Value option : options) {
                            RadioButton rb = new RadioButton(option.getStringValue());
                            panel.addChild(rb);
                        }
                        drawingPanel.addChild(panel);
                    }
                }
            }
        }
    }
}