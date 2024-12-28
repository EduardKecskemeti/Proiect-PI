package ui;

import javax.swing.*;
import java.awt.*;

public class WorkoutSuggestionsPage extends JFrame {

    public WorkoutSuggestionsPage() {
        setTitle("Workout Suggestions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);

        // Initialize components
        JLabel label = new JLabel("Workout Suggestions Page");
        add(label);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}