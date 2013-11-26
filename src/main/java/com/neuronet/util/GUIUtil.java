package com.neuronet.util;

import javax.swing.*;

public class GUIUtil {

    public static JFrame createFrame(final JPanel chartPanel) {
        final JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(chartPanel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
        return frame;
    }
}