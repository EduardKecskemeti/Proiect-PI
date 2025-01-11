// src/main/java/ui/MainPage.java
package ui;

import database.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainPage extends JFrame {

    private JButton workoutSuggestionsButton;
    private JButton macroTrackerButton;
    private JButton achievementsButton;
    private JButton addWorkoutButton;
    private UserRepository userRepository;
    private String username;
    private JProgressBar calorieProgressBar;

    public MainPage(String username, UserRepository userRepository) {
        this.username = username;
        this.userRepository = userRepository;

        setTitle("Main Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);

        // Initialize components
        JPanel topPanel = new JPanel(new FlowLayout());
        addWorkoutButton = new JButton("Add workout");
        workoutSuggestionsButton = new JButton("Workout Suggestions");
        macroTrackerButton = new JButton("Macro Tracker");
        achievementsButton = new JButton("Achievements");

        // Add buttons to top panel
        topPanel.add(addWorkoutButton);
        topPanel.add(workoutSuggestionsButton);
        topPanel.add(macroTrackerButton);
        topPanel.add(achievementsButton);

        // Create center panel for workout summary and calorie bar
        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel workoutSummaryLabel = new JLabel("Weekly Workout Summary");
        JTable muscleGroupTable = createMuscleGroupTable();

        // Add components to center panel
        centerPanel.add(workoutSummaryLabel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(muscleGroupTable), BorderLayout.CENTER);

        // Create calorie progress bar
        calorieProgressBar = new JProgressBar(0, 100);
        calorieProgressBar.setStringPainted(true);
        updateCalorieProgressBar();

        // Add calorie progress bar to center panel
        centerPanel.add(calorieProgressBar, BorderLayout.SOUTH);

        // Set layout and add panels to frame
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Add action listeners to buttons
        addWorkoutButton.addActionListener(e -> openAddWorkoutPage());
        workoutSuggestionsButton.addActionListener(e -> openWorkoutSuggestionsPage());
        macroTrackerButton.addActionListener(e -> openMacroTrackerPage());
        achievementsButton.addActionListener(e -> openAchievementsPage());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JTable createMuscleGroupTable() {
        String[] columnNames = {"Muscle Group", "Sets"};
        List<Object[]> data = new ArrayList<>();

        // Initialize all muscle groups with zero sets
        Map<String, String[]> muscleExercises = Map.of(
                "Chest", new String[]{"Bench Press", "Incline Bench", "Chest Flies", "Push Ups"},
                "Back", new String[]{"Deadlifts", "Lat Pulldowns", "Rows", "Pullovers", "Pullups"},
                "Biceps", new String[]{"Bicep Curls", "Hammer Curls", "Preacher Curls", "Behind the Back Curls"},
                "Triceps", new String[]{"Tricep Pushdowns", "Tricep Extensions", "Dips", "Overhead Extensions", "JM Press", "Close Grip Bench"},
                "Shoulders", new String[]{"Shoulder Press", "Lateral Raises", "Upright Rows", "Reverse Flies", "Front Raises", "Face Pulls"},
                "Quads", new String[]{"Squats", "Hack Squats", "Leg Extensions", "Leg Press"},
                "Hamstrings", new String[]{"Seated Hamstring Curl", "Lying Hamstring Curl", "Stiff Legged Deadlifts"},
                "Glutes", new String[]{"Romanian Deadlifts", "Hip Thrusts", "Glute Kickbacks"},
                "Calves", new String[]{"Toe Press", "Standing Calf Raise", "Sitting Calf Raise"}
        );

        muscleExercises.keySet().forEach(muscleGroup -> data.add(new Object[]{muscleGroup, 0}));

        try {
            Map<String, Integer> muscleGroupSets = userRepository.getAllMuscleGroupSets(username);
            muscleGroupSets.forEach((muscleGroup, sets) -> {
                for (Object[] row : data) {
                    if (row[0].equals(muscleGroup)) {
                        row[1] = sets;
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        data.sort((e1, e2) -> Integer.compare((int) e2[1], (int) e1[1]));
        Object[][] dataArray = data.toArray(new Object[0][]);
        return new JTable(dataArray, columnNames);
    }

    private void openAddWorkoutPage() {
        new AddWorkoutPage(username);
    }

    private void openWorkoutSuggestionsPage() {
        new WorkoutSuggestionsPage();
    }

    private void openMacroTrackerPage() {
        new MacroTrackerPage(username, userRepository,this);
    }

    private void openAchievementsPage() {
        new AchievementsPage(username);
    }

    public void updateCalorieProgressBar() {
        int consumedCalories = userRepository.getConsumedCalories(username);
        double dailyCalorieGoal = userRepository.calculateDailyCalorieIntake(username);
        int progress = (int) ((consumedCalories / dailyCalorieGoal) * 100);
        calorieProgressBar.setValue(progress);
        calorieProgressBar.setString(consumedCalories + " / " + (int) dailyCalorieGoal + " kcal");
    }
}