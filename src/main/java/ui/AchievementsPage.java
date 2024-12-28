package ui;

import javax.swing.*;
import java.awt.*;

public class AchievementsPage extends JFrame {

    public AchievementsPage() {
        setTitle("Achievements");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);

        // Initialize components
        JLabel label = new JLabel("Achievements Page");
        add(label);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}