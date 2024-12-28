package ui;

import javax.swing.*;
import java.awt.*;

public class MacroTrackerPage extends JFrame {

    public MacroTrackerPage() {
        setTitle("Macro Tracker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);

        // Initialize components
        JLabel label = new JLabel("Macro Tracker Page");
        add(label);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}