package ui.toolkit;

import java.awt.*;

import javax.swing.*;

import ui.toolkit.behavior.InteractiveWindowGroup;
import ui.toolkit.graphics.object.Line;
import ui.toolkit.widget.PropertySheet;

public class PropertySheetTest extends InteractiveWindowGroup {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new PropertySheetTest();
    }

    public PropertySheetTest() {
        super("Property Sheet Test", 400, 300);
        Line comp = new Line(100, 0, 100, 300, Color.BLACK, 2);
        addChild(comp);

        PropertySheet editor = new PropertySheet(comp, this);
        JComponent pane = new JScrollPane(editor);
        pane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, pane);

    }

}
