package ui.toolkit.widget;

import java.awt.*;

/**
 * A layout manager that lays out components along a central axis
 */
class FormLayout implements LayoutManager {
    public Dimension preferredLayoutSize(Container parent) {
        Component[] components = parent.getComponents();
        left = 0;
        right = 0;
        height = 0;
        for (int i = 0; i < components.length; i += 2) {
            Component cLeft = components[i];
            Component cRight = components[i + 1];

            Dimension dLeft = cLeft.getPreferredSize();
            Dimension dRight = cRight.getPreferredSize();
            left = Math.max(left, dLeft.width);
            right = Math.max(right, dRight.width);
            height = height + Math.max(dLeft.height, dRight.height);
        }
        return new Dimension(left + GAP + right, height);
    }

    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    public void layoutContainer(Container parent) {
        preferredLayoutSize(parent); // sets left, right

        Component[] components = parent.getComponents();

        Insets insets = parent.getInsets();
        int xCenter = insets.left + left;
        int y = insets.top;

        for (int i = 0; i < components.length; i += 2) {
            Component cLeft = components[i];
            Component cRight = components[i + 1];

            Dimension dLeft = cLeft.getPreferredSize();
            Dimension dRight = cRight.getPreferredSize();

            int height = Math.max(dLeft.height, dRight.height);

            cLeft.setBounds(xCenter - dLeft.width, y + (height - dLeft.height) / 2, dLeft.width, dLeft.height);

            cRight.setBounds(xCenter + GAP, y + (height - dRight.height) / 2, dRight.width, dRight.height);
            y += height;
        }
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    private int left;
    private int right;
    private int height;
    private static final int GAP = 6;
}
