package ui;

import database.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MacroTrackerPage extends JFrame {

    private JProgressBar calorieProgressBar;
    private JLabel calorieIntakeLabel;
    private JTextField mealNameField;
    private JTextField mealCaloriesField;
    private JButton logMealButton;
    private JTextArea mealLogArea;
    private UserRepository userRepository;
    private String username;

    public MacroTrackerPage(String username, UserRepository userRepository) {
        this.username = username;
        this.userRepository = userRepository;

        setTitle("Macro Tracker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);

        // Customize progress bar appearance
        UIManager.put("ProgressBar.foreground", new Color(76, 175, 80));
        UIManager.put("ProgressBar.background", new Color(200, 200, 200));
        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
        UIManager.put("ProgressBar.selectionBackground", Color.WHITE);
        UIManager.put("ProgressBar.border", BorderFactory.createLineBorder(Color.GRAY, 1));

        // Initialize components
        calorieProgressBar = new JProgressBar();
        calorieProgressBar.setStringPainted(true);
        calorieIntakeLabel = new JLabel();
        mealNameField = new JTextField(10);
        mealCaloriesField = new JTextField(10);
        logMealButton = new JButton("Log Meal");
        mealLogArea = new JTextArea(10, 30);
        mealLogArea.setEditable(false);

        // Calculate daily calorie intake
        double dailyCalorieIntake = userRepository.calculateDailyCalorieIntake(username);
        calorieIntakeLabel.setText("Daily Calorie Intake: " + dailyCalorieIntake + " kcal");

        // Get consumed calories
        int consumedCalories = userRepository.getConsumedCalories(username);
        calorieProgressBar.setMaximum((int) dailyCalorieIntake);
        calorieProgressBar.setValue(consumedCalories);

        // Layout setup using GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Add calorie intake label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(calorieIntakeLabel, gbc);

        // Add calorie progress bar
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(calorieProgressBar, gbc);

        // Add meal name label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Meal Name:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(mealNameField, gbc);

        // Add meal calories label and field
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Calories:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(mealCaloriesField, gbc);

        // Add log meal button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(logMealButton, gbc);

        // Add meal log area
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        gbc.gridwidth = 1;
        mainPanel.add(new JScrollPane(mealLogArea), gbc);

        // Add main panel to frame
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Add action listener to log meal button
        logMealButton.addActionListener(e -> logMeal());
        updateMealLog();
    }

    private void logMeal() {
        String mealName = mealNameField.getText();
        int calories = Integer.parseInt(mealCaloriesField.getText());
        if (userRepository.logMeal(username, mealName, calories)) {
            userRepository.logConsumedCalories(username, calories);
            updateConsumedCalories();
            updateMealLog();
            mealNameField.setText("");
            mealCaloriesField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to log meal.");
        }
    }

    private void updateConsumedCalories() {
        int consumedCalories = userRepository.getConsumedCalories(username);
        calorieProgressBar.setValue(consumedCalories);
    }

    private void updateMealLog() {
        ResultSet rs = userRepository.getMeals(username);
        mealLogArea.setText("");
        try {
            while (rs != null && rs.next()) {
                String mealName = rs.getString("meal_name");
                int calories = rs.getInt("calories");
                mealLogArea.append(mealName + ": " + calories + " kcal\n");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}