package ui.talk;

import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import ui.toolkit.behavior.BehaviorEvent;
import ui.toolkit.behavior.MoveBehavior;
import ui.toolkit.behavior.NewRectBehavior;
import ui.toolkit.graphics.group.Group;
import ui.toolkit.graphics.object.*;
import ui.toolkit.graphics.object.selectable.SelectableFilledEllipse;
import ui.toolkit.widget.RadioButton;
import ui.toolkit.widget.RadioButtonPanel;

import java.awt.*;
import java.util.List;

import static ui.toolkit.behavior.BehaviorEvent.*;

class ResponseHandler {

    public GraphicalObject handle(QueryResult queryResult, Group drawingPanel) {
        String intentName = queryResult.getIntent().getDisplayName();
        Struct params = queryResult.getParameters();

        if (intentName.equals(Intent.INITIALIZATION)) {
            return handleInstantiation(params, drawingPanel);
        } else if (intentName.equals((Intent.INTERACTION))) {
            handleInteraction(params, drawingPanel);
            return null;
        } else {
            System.err.println("Unrecognized intent: " + intentName);
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
                return new SelectableFilledEllipse(20, 20, width, height, Entity.stringToColor.get(color));
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

                if (values.size() == 0) {
                    radios.addChild(new RadioButton("Pizza"));
                    radios.addChild(new RadioButton("Pasta"));
                    radios.addChild(new RadioButton("Salad"));
                } else {
                    if (values.size() == 1) {
                        // attempt to split up text
                        String[] vs = values.get(0).getStringValue().split(" ");
                        for (String value : vs) {
                            radios.addChild(new RadioButton(value));
                        }
                    } else {
                        for (Value value : values) {
                            String option = value.getStringValue();
                            radios.addChild(new RadioButton(option));
                        }
                    }
                }

                return radios;
            }

            default: {
                System.err.println("Unrecognized/Unsupported (in demo) graphical object type: " + newObjectType);
                return null;
            }
        }
    }


    private void handleInteraction(Struct params, Group drawingPanel) {

        Value behavior = params.getFieldsOrDefault(Intent.InteractionParams.BEHAVIOR, null);

        if (behavior != null) { // DialogFlow will ensure that this param is provided
            switch (behavior.getStringValue()) {

                case "ChoiceBehavior": {
                    System.out.println("Adding choice behavior");

                    // We need to figure out the target
                    // for now, the target is the same object selected, unless they mention "circle 1" as the target
                    // (since we can't get this information in our DialogFlow intent for now)

                    String targetName = params.getFieldsOrDefault(Intent.InteractionParams.BEHAVIOR_TARGET, null).getStringValue();

                    GraphicalObject targetObject = null;

                    if (targetName.toLowerCase().equals("circle 1")) {
                        System.out.println("Using manual override circle 1");
                        List<GraphicalObject> children = drawingPanel.getChildren();

                        // find the circle we want for the demo
                        for (GraphicalObject go: children) {
                            if (go instanceof FilledEllipse) {
                                if (((FilledEllipse) go).getHeight() > 100) {
                                    System.out.println("Found the circle we want");
                                    targetObject = go;
                                }
                            }
                        }
                    }

                    TalkUI.Instance.needsSelection = true;
                    TalkUI.Instance.interactionTarget = targetObject;

                    String num = params.getFieldsOrDefault(Intent.InteractionParams.NUMBER_INTEGER, null).getStringValue();
                    String color = params.getFieldsOrDefault(Intent.InteractionParams.COLOR, null).getStringValue();

                    String property = params.getFieldsOrDefault(Intent.InteractionParams.GRAPHICS_PARAMETER, null).getStringValue();
                    String value = "";

                    if (!num.equals("")) {
                        value = num;
                    }

                    if (!color.equals("")) {
                        value = color;
                        property = "color";
                    }

                    TalkUI.Instance.interactionOutcome = new InteractionOutcome(null, property, value);

                    break;
                }

                case "MoveBehavior": {
                    System.out.println("Adding move behavior");

                    // for the purposes of the demo, use the MoveBehavior
                    // already on the canvas, since we do not have a full play mode
                    drawingPanel.addBehavior(new MoveBehavior());

                    break;
                }

                case "NewBehavior": {
                    // attach the new behavior to the base group
                    System.out.println("Adding new behavior");

                    drawingPanel.addBehavior(new NewRectBehavior(NewRectBehavior.OUTLINE_RECT, Color.YELLOW, 2)
                            .setGroup(drawingPanel)
                            .setStartEvent(new BehaviorEvent(CONTROL_MODIFIER, LEFT_MOUSE_KEY, MOUSE_DOWN_ID))
                            .setStopEvent(new BehaviorEvent(ANY_MODIFIER, LEFT_MOUSE_KEY, MOUSE_UP_ID))
                            .setCancelEvent(new BehaviorEvent(NO_MODIFIER, 'z', KEY_UP_ID)));

                    // set priority

                    break;
                }

                default: {
                    System.err.println("Unrecognized behavior: " + behavior);
                    break;
                }
            }
        }
    }
}