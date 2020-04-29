package ui.talk;

import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import ui.toolkit.graphics.group.Group;
import ui.toolkit.graphics.object.FilledEllipse;
import ui.toolkit.graphics.object.FilledRect;
import ui.toolkit.graphics.object.GraphicalObject;
import ui.toolkit.graphics.object.Text;
import ui.toolkit.widget.RadioButton;
import ui.toolkit.widget.RadioButtonPanel;

import java.util.List;

class ResponseHandler {
    public GraphicalObject handle(QueryResult queryResult, Group drawingPanel) {
        String intentName = queryResult.getIntent().getDisplayName();
        Struct params = queryResult.getParameters();

        if (intentName.equals(Intent.INITIALIZATION)) {
            return handleInstantiation(params, drawingPanel);
        } else if (intentName.equals((Intent.INTERACTION))) {
            return null; // TODO
        } else {
            System.err.println("Unrecognized intent");
            return null;
        }
    }

    private GraphicalObject handleInstantiation(Struct params, Group drawingPanel) {
        Value newObject = params.getFieldsOrDefault(Intent.InitializationParams.GRAPHICAL_OBJECT, null);
        if (newObject == null) {
            return null;
        }

        String newObjectType = newObject.getStringValue();
        switch (newObjectType) {
            case Entity.GraphicalObjectType.FILLED_RECT: {
                int width = (int) params.getFieldsOrDefault(Intent.InitializationParams.WIDTH, null).getNumberValue();
                int height = (int) params.getFieldsOrDefault(Intent.InitializationParams.HEIGHT, null).getNumberValue();
                String color = params.getFieldsOrDefault(Intent.InitializationParams.COLOR, null).getStringValue();
                System.out.println("Drawing FilledRect: " + width + " " + height + " " + color);
                return new FilledRect(20, 20, width, height, Entity.stringToColor.get(color));
            }

            case Entity.GraphicalObjectType.FILLED_ELLIPSE: {
                int width = (int) params.getFieldsOrDefault(Intent.InitializationParams.WIDTH, null).getNumberValue();
                int height = (int) params.getFieldsOrDefault(Intent.InitializationParams.HEIGHT, null).getNumberValue();
                String color = params.getFieldsOrDefault(Intent.InitializationParams.COLOR, null).getStringValue();
                System.out.println("Drawing FilledEllipse: " + width + " " + height + " " + color);
                return new FilledEllipse(20, 20, width, height, Entity.stringToColor.get(color));
            }

            case Entity.GraphicalObjectType.TEXT: {
                String text = params.getFieldsOrDefault(Intent.InitializationParams.TEXT, null).getStringValue();
                String color = params.getFieldsOrDefault(Intent.InitializationParams.COLOR, null).getStringValue();
                System.out.println("Drawing Text: " + text + " " + color);
                return new Text(text, 20, 20, Text.DEFAULT_FONT, Entity.stringToColor.get(color));
            }

            case Entity.GraphicalObjectType.RADIOBUTTON_PANEL: {
                List<Value> values = params.getFieldsOrDefault(Intent.InitializationParams.OPTIONS, null).getListValue()
                        .getValuesList();

                System.out.println("Drawing RadioButton: " + values.size());
                RadioButtonPanel radios = new RadioButtonPanel(20, 20);

                for (Value value : values) {
                    String option = value.getStringValue();
                    radios.addChild(new RadioButton(option));
                }
                return radios;
            }

            default: {
                System.err.println("Unrecognized graphical object type: " + newObjectType);
                return null;
            }
        }
    }
}