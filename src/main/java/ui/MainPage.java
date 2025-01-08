// src/main/java/ui/MainPage.java
package ui;

import database.UserRepository;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {

    private JButton workoutSuggestionsButton;
    private JButton macroTrackerButton;
    private JButton achievementsButton;
    private JButton workoutProgressButton;
    private UserRepository userRepository;
    private String username;

    public MainPage(String username, UserRepository userRepository) {
        this.username = username;
        this.userRepository = userRepository;

        setTitle("Main Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);

        // Initialize components
        JPanel topPanel = new JPanel(new FlowLayout());
        workoutProgressButton = new JButton("Workout Progress");
        workoutSuggestionsButton = new JButton("Workout Suggestions");
        macroTrackerButton = new JButton("Macro Tracker");
        achievementsButton = new JButton("Achievements");

        // Add buttons to top panel
        topPanel.add(workoutProgressButton);
        topPanel.add(workoutSuggestionsButton);
        topPanel.add(macroTrackerButton);
        topPanel.add(achievementsButton);

        // Create center panel for workout summary and calorie bar
        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel workoutSummaryLabel = new JLabel("Weekly Workout Summary");
        JProgressBar calorieBar = new JProgressBar();
        calorieBar.setStringPainted(true);
        calorieBar.setValue(50); // Example value

        // Add components to center panel
        centerPanel.add(workoutSummaryLabel, BorderLayout.NORTH);
        centerPanel.add(calorieBar, BorderLayout.CENTER);

        // Set layout and add panels to frame
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Add action listeners to buttons
        workoutProgressButton.addActionListener(e -> openWorkoutProgressPage());
        workoutSuggestionsButton.addActionListener(e -> openWorkoutSuggestionsPage());
        macroTrackerButton.addActionListener(e -> openMacroTrackerPage());
        achievementsButton.addActionListener(e -> openAchievementsPage());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openWorkoutProgressPage() {
        new WorkoutProgressPage(username);
    }

    private void openWorkoutSuggestionsPage() {
        new WorkoutSuggestionsPage();
    }

    private void openMacroTrackerPage() {
        new MacroTrackerPage(username, userRepository);
    }

    private void openAchievementsPage() {
        new AchievementsPage(username);
    }
}