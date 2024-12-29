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
    private JTextField mealProteinsField;
    private JTextField mealFatsField;
    private JTextField mealCarbohydratesField;
    private JButton logMealButton;
    private JTextArea mealLogArea;
    private JLabel totalCaloriesLabel;
    private JLabel totalProteinsLabel;
    private JLabel totalFatsLabel;
    private JLabel totalCarbohydratesLabel;
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
        mealProteinsField = new JTextField(10);
        mealFatsField = new JTextField(10);
        mealCarbohydratesField = new JTextField(10);
        logMealButton = new JButton("Log Meal");
        mealLogArea = new JTextArea(10, 30);
        mealLogArea.setEditable(false);

        // Initialize total labels
        totalCaloriesLabel = new JLabel("Total Calories: 0");
        totalProteinsLabel = new JLabel("Total Proteins: 0g");
        totalFatsLabel = new JLabel("Total Fats: 0g");
        totalCarbohydratesLabel = new JLabel("Total Carbohydrates: 0g");

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

        // Add meal proteins label and field
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Proteins:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(mealProteinsField, gbc);

        // Add meal fats label and field
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Fats:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        mainPanel.add(mealFatsField, gbc);

        // Add meal carbohydrates label and field
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("Carbohydrates:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        mainPanel.add(mealCarbohydratesField, gbc);

        // Add log meal button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        mainPanel.add(logMealButton, gbc);

        // Add meal log area
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 8;
        gbc.gridwidth = 1;
        mainPanel.add(new JScrollPane(mealLogArea), gbc);

        // Add total labels
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        mainPanel.add(totalCaloriesLabel, gbc);
        gbc.gridy = 1;
        mainPanel.add(totalProteinsLabel, gbc);
        gbc.gridy = 2;
        mainPanel.add(totalFatsLabel, gbc);
        gbc.gridy = 3;
        mainPanel.add(totalCarbohydratesLabel, gbc);

        // Add main panel to frame
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Add action listener to log meal button
        logMealButton.addActionListener(e -> logMeal());
        updateMealLog();
        updateConsumedMacros();
    }

    private void logMeal() {
        String mealName = mealNameField.getText();
        int calories = Integer.parseInt(mealCaloriesField.getText());
        int proteins = Integer.parseInt(mealProteinsField.getText());
        int fats = Integer.parseInt(mealFatsField.getText());
        int carbohydrates = Integer.parseInt(mealCarbohydratesField.getText());
        if (userRepository.logMeal(username, mealName, calories, proteins, fats, carbohydrates)) {
            userRepository.logConsumedCalories(username, calories);
            updateConsumedCalories();
            updateConsumedMacros();
            updateMealLog();
            mealNameField.setText("");
            mealCaloriesField.setText("");
            mealProteinsField.setText("");
            mealFatsField.setText("");
            mealCarbohydratesField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to log meal.");
        }
    }

    private void updateConsumedCalories() {
        int consumedCalories = userRepository.getConsumedCalories(username);
        calorieProgressBar.setValue(consumedCalories);
    }

    private void updateConsumedMacros() {
        int totalProteins = userRepository.getConsumedProteins(username);
        int totalFats = userRepository.getConsumedFats(username);
        int totalCarbohydrates = userRepository.getConsumedCarbohydrates(username);

        totalProteinsLabel.setText("Total Proteins: " + totalProteins + "g");
        totalFatsLabel.setText("Total Fats: " + totalFats + "g");
        totalCarbohydratesLabel.setText("Total Carbohydrates: " + totalCarbohydrates + "g");
    }

    private void updateMealLog() {
        ResultSet rs = userRepository.getMeals(username);
        mealLogArea.setText("");
        int totalCalories = userRepository.getConsumedCalories(username);
        int totalProteins = userRepository.getConsumedProteins(username);
        int totalFats = userRepository.getConsumedFats(username);
        int totalCarbohydrates = userRepository.getConsumedCarbohydrates(username);
        try {
            while (rs != null && rs.next()) {
                String mealName = rs.getString("meal_name");
                int calories = rs.getInt("calories");
                int proteins = rs.getInt("proteins");
                int fats = rs.getInt("fats");
                int carbohydrates = rs.getInt("carbohydrates");
                mealLogArea.append(mealName + ": " + calories + " kcal, " + proteins + "g proteins, " + fats + "g fats, " + carbohydrates + "g carbs\n");
                totalCalories += calories;
                totalProteins += proteins;
                totalFats += fats;
                totalCarbohydrates += carbohydrates;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        totalCaloriesLabel.setText("Total Calories: " + totalCalories);
        totalProteinsLabel.setText("Total Proteins: " + totalProteins + "g");
        totalFatsLabel.setText("Total Fats: " + totalFats + "g");
        totalCarbohydratesLabel.setText("Total Carbohydrates: " + totalCarbohydrates + "g");
    }
}