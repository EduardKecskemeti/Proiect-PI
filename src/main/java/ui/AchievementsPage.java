// src/main/java/ui/AchievementsPage.java
package ui;

import datastructures.RedBlackTree;
import database.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AchievementsPage extends JFrame {

    private UserRepository userRepository;
    private String username;
    private RedBlackTree<String> exerciseTree;

    public AchievementsPage(String username) {
        this.username = username;
        try {
            userRepository = new UserRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        exerciseTree = new RedBlackTree<>();

        setTitle("Achievements");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));
        setResizable(false);

        JLabel titleLabel = new JLabel("Achievements", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));

        JPanel centerPanel = new JPanel(new BorderLayout());

        String[] columnNames = {"Exercise", "Max Weight (kg)"};
        Object[][] data = loadExerciseData();

        JTable achievementsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(achievementsTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Object[][] loadExerciseData() {
        String[] exercises = {
                "Bench Press", "Incline Bench", "Deadlifts", "Rows",
                "Bicep Curls", "Dips", "Squats", "Hack Squats",
                "Romanian Deadlifts", "Hip Thrusts"
        };

        Object[][] data = new Object[exercises.length][2];

        for (int i = 0; i < exercises.length; i++) {
            String exercise = exercises[i];
            int maxWeight = 0;
            try {
                maxWeight = userRepository.getMaxWeightForExercise(username, exercise);
                exerciseTree.insert(exercise);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error retrieving max weight for " + exercise + ".", "Error", JOptionPane.ERROR_MESSAGE);
            }
            data[i][0] = exercise;
            data[i][1] = maxWeight;
        }

        return data;
    }
}