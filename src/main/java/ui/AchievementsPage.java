// src/main/java/ui/AchievementsPage.java
package ui;

import database.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AchievementsPage extends JFrame {

    private UserRepository userRepository;
    private String[] exercises = {"Bench Press", "Squat", "Deadlift", "Pull Up", "Bicep Curl"};

    public AchievementsPage(String username) {
        try {
            userRepository = new UserRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setTitle("Achievements");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));
        setResizable(false);

        // Initialize components
        JLabel titleLabel = new JLabel("Achievements", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Fetch and display the max weight for each exercise
        Map<String, Integer> maxWeights = new HashMap<>();
        for (String exercise : exercises) {
            try {
                int maxWeight = userRepository.getMaxWeightForExercise(username, exercise);
                maxWeights.put(exercise, maxWeight);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error retrieving max weight for " + exercise + ".", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        for (Map.Entry<String, Integer> entry : maxWeights.entrySet()) {
            JLabel exerciseLabel = new JLabel(entry.getKey() + ": " + entry.getValue() + " kg");
            centerPanel.add(exerciseLabel);
        }

        // Layout setup
        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}