package ui.toolkit;

import java.awt.*;

import javax.swing.*;

import ui.toolkit.behavior.InteractiveWindowGroup;
import ui.toolkit.graphics.object.GraphicalObject;
import ui.toolkit.graphics.object.Line;
import ui.toolkit.widget.PropertySheet;
import ui.toolkit.widget.RadioButton;
import ui.toolkit.widget.RadioButtonPanel;

public class PropertySheetTest extends InteractiveWindowGroup {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new PropertySheetTest();
    }

    public PropertySheetTest() {
        super("Property Sheet Test", 800, 600);

        RadioButtonPanel radioPanel = new RadioButtonPanel(50, 50)
                .addChildren(new RadioButton(new Line(0, 10, 40, 10, Color.BLACK, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.BLUE, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.MAGENTA, 3)),
                        new RadioButton(new Line(0, 10, 40, 10, Color.CYAN, 3)))
                .setSelection("one");

        addChild(radioPanel);

        PropertySheet editor = new PropertySheet(radioPanel.getChildren().get(0), this);
        JComponent pane = new JScrollPane(editor);

        radioPanel.setCallback(o -> {
            for (GraphicalObject child : radioPanel.getChildren()) {
                if (((RadioButton) child).isSelected()) {
                    System.out.println("update selection...");
                    editor.updatePropertySheet(child);
                }
            }
        });

        pane.setBounds(200, 100, 400, 300);
        getCanvas().add(pane);
        redraw();
    }
}
