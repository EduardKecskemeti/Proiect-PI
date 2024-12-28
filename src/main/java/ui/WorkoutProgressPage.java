package ui;

import javax.swing.*;
import java.awt.*;

public class WorkoutProgressPage extends JFrame {

    public WorkoutProgressPage() {
        setTitle("Workout Progress");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);

        // Initialize components
        JLabel label = new JLabel("Workout Progress Page");
        add(label);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}