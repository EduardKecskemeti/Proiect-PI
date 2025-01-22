// src/main/java/ui/MacroTrackerPage.java
package ui;

import database.UserRepository;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JFrame;

import javax.swing.JComboBox;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MacroTrackerPage extends JFrame {
    private MainPage mainPage;
    private JProgressBar calorieProgressBar;
    private JLabel calorieIntakeLabel;
    private JTextField mealCaloriesField;
    private JTextField mealProteinsField;
    private JTextField mealFatsField;
    private JTextField mealCarbohydratesField;
    private JTextField mealAmountField;
    private JButton logMealButton;
    private JButton resetButton;
    private JTextArea mealLogArea;
    private JProgressBar proteinProgressBar;
    private JProgressBar fatProgressBar;
    private JProgressBar carbohydrateProgressBar;
    private JLabel proteinLabel;
    private JLabel fatLabel;
    private JLabel carbohydrateLabel;
    private UserRepository userRepository;
    private String username;
    private JComboBox<String> presetFoodComboBox;
    private JButton logPresetFoodButton;
    private JButton addCustomFoodButton;

    public MacroTrackerPage(String username, UserRepository userRepository, MainPage mainPage) {
        this.username = username;
        this.userRepository = userRepository;
        this.mainPage = mainPage;

        setTitle("Macro Tracker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);

        // Customize progress bar appearance
        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
        UIManager.put("ProgressBar.selectionBackground", Color.WHITE);
        UIManager.put("ProgressBar.border", BorderFactory.createLineBorder(Color.GRAY, 1));

        // Initialize components
        calorieProgressBar = new JProgressBar();
        calorieProgressBar.setStringPainted(true);
        calorieIntakeLabel = new JLabel();
        mealCaloriesField = new JTextField(10);
        mealCaloriesField.setEditable(false);
        mealProteinsField = new JTextField(10);
        mealFatsField = new JTextField(10);
        mealCarbohydratesField = new JTextField(10);
        mealAmountField = new JTextField(10);
        logMealButton = new JButton("Log Meal");
        resetButton = new JButton("Reset");
        mealLogArea = new JTextArea(10, 30);
        mealLogArea.setEditable(false);
        presetFoodComboBox = new JComboBox<>();
        addCustomFoodButton = new JButton("Add Custom Food");
        addCustomFoodButton.addActionListener(e -> new AddCustomFoodPage(userRepository,this));
        logPresetFoodButton = new JButton("Log Preset Food");
        try {
            List<Map<String, Object>> presetFoods = userRepository.getPresetFoods();
            for (Map<String, Object> food : presetFoods) {
                presetFoodComboBox.addItem((String) food.get("food_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Initialize progress bars for macronutrients
        proteinProgressBar = new JProgressBar(SwingConstants.VERTICAL);
        proteinProgressBar.setStringPainted(true);
        proteinProgressBar.setForeground(Color.RED);
        fatProgressBar = new JProgressBar(SwingConstants.VERTICAL);
        fatProgressBar.setStringPainted(true);
        fatProgressBar.setForeground(new Color(128, 0, 128)); // Purple
        carbohydrateProgressBar = new JProgressBar(SwingConstants.VERTICAL);
        carbohydrateProgressBar.setStringPainted(true);
        carbohydrateProgressBar.setForeground(Color.YELLOW);

        // Initialize labels for macronutrients
        proteinLabel = new JLabel("Proteins");
        fatLabel = new JLabel("Fats");
        carbohydrateLabel = new JLabel("Carbohydrates");

        // Calculate daily calorie intake
        double dailyCalorieIntake = userRepository.calculateDailyCalorieIntake(username);

        // Calculate maximum values for macronutrient progress bars
        int maxCarbs = (int) (dailyCalorieIntake * 0.50 / 4);
        int maxFats = (int) (dailyCalorieIntake * 0.15 / 9);
        int maxProteins = (int) (dailyCalorieIntake * 0.35 / 4);

        // Set maximum values for progress bars
        proteinProgressBar.setMaximum(maxProteins);
        fatProgressBar.setMaximum(maxFats);
        carbohydrateProgressBar.setMaximum(maxCarbs);

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

        // Add preset food combo box
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Preset Foods:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(presetFoodComboBox, gbc);

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

        // Add meal amount label and field
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(new JLabel("Amount (grams):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 7;
        mainPanel.add(mealAmountField, gbc);

        // Add log meal button
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        mainPanel.add(logMealButton, gbc);

        // Add reset button
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        mainPanel.add(resetButton, gbc);

        // Add meal log area
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 10;
        gbc.gridwidth = 1;
        mainPanel.add(new JScrollPane(mealLogArea), gbc);

        // Add horizontal spacer
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 10;
        gbc.gridwidth = 1;
        mainPanel.add(Box.createHorizontalStrut(20), gbc);

        // Add macronutrient labels
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        mainPanel.add(new JLabel("Proteins"), gbc);
        gbc.gridx = 5;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Fats"), gbc);
        gbc.gridx = 6;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Carbohydrates"), gbc);

        // Add macronutrient progress bars and labels
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.gridheight = 8;
        gbc.gridwidth = 1;
        mainPanel.add(proteinProgressBar, gbc);
        gbc.gridy = 9;
        gbc.gridheight = 1;
        mainPanel.add(proteinLabel, gbc);

        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.gridheight = 8;
        mainPanel.add(fatProgressBar, gbc);
        gbc.gridy = 9;
        gbc.gridheight = 1;
        mainPanel.add(fatLabel, gbc);

        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.gridheight = 8;
        mainPanel.add(carbohydrateProgressBar, gbc);
        gbc.gridy = 9;
        gbc.gridheight = 1;
        mainPanel.add(carbohydrateLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        mainPanel.add(addCustomFoodButton, gbc);

        // Add main panel to frame
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Add action listener to log meal button
        logMealButton.addActionListener(e -> logMeal());

        // Add action listener to reset button
        resetButton.addActionListener(e -> resetMacros());
        // Add action listener to preset food combo box
        presetFoodComboBox.addActionListener(e -> fillMacroFields());
        // Add key listener to meal amount field
        mealAmountField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateMacroFields();
            }
        });
        // Add document listeners to macro fields to update calories
        mealProteinsField.getDocument().addDocumentListener(new MacroFieldListener());
        mealFatsField.getDocument().addDocumentListener(new MacroFieldListener());
        mealCarbohydratesField.getDocument().addDocumentListener(new MacroFieldListener());

        updateMealLog();
        updateConsumedMacros();
        updateConsumedCalories();
    }


    private void fillMacroFields() {
        String selectedFood = (String) presetFoodComboBox.getSelectedItem();
        if (selectedFood != null) {
            try {
                Map<String, Object> foodData = userRepository.getPresetFoodData(selectedFood);
                int calories = (int) foodData.get("calories");
                int proteins = (int) foodData.get("proteins");
                int fats = (int) foodData.get("fats");
                int carbohydrates = (int) foodData.get("carbohydrates");

                mealCaloriesField.setText(String.valueOf(calories));
                mealProteinsField.setText(String.valueOf(proteins));
                mealFatsField.setText(String.valueOf(fats));
                mealCarbohydratesField.setText(String.valueOf(carbohydrates));
                mealAmountField.setText("100");

                updateMacroFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMacroFields() {
        try {
            int amount = Integer.parseInt(mealAmountField.getText());
            String selectedFood = (String) presetFoodComboBox.getSelectedItem();
            if (selectedFood != null) {
                Map<String, Object> foodData = userRepository.getPresetFoodData(selectedFood);
                int calories = (int) foodData.get("calories");
                int proteins = (int) foodData.get("proteins");
                int fats = (int) foodData.get("fats");
                int carbohydrates = (int) foodData.get("carbohydrates");

                mealCaloriesField.setText(String.valueOf(calories * amount / 100));
                mealProteinsField.setText(String.valueOf(proteins * amount / 100));
                mealFatsField.setText(String.valueOf(fats * amount / 100));
                mealCarbohydratesField.setText(String.valueOf(carbohydrates * amount / 100));
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void logMeal() {
        String mealName = (String) presetFoodComboBox.getSelectedItem();
        int calories = Integer.parseInt(mealCaloriesField.getText());
        int proteins = Integer.parseInt(mealProteinsField.getText());
        int fats = Integer.parseInt(mealFatsField.getText());
        int carbohydrates = Integer.parseInt(mealCarbohydratesField.getText());
        int amount = Integer.parseInt(mealAmountField.getText());

        if (userRepository.logMeal(username, mealName, calories, proteins, fats, carbohydrates, amount)) {
            updateConsumedMacros();
            updateConsumedCalories();
            updateMealLog();
        }
        mainPage.updateCalorieProgressBar();

    }

    private void updateConsumedCalories() {
        int consumedCalories = userRepository.getConsumedCalories(username);
        calorieProgressBar.setValue(consumedCalories);
        int maxCalories = calorieProgressBar.getMaximum();
        calorieIntakeLabel.setText("Daily Calorie Intake: " + consumedCalories + " / " + maxCalories + " kcal");
    }

    private void updateConsumedMacros() {
        int totalProteins = userRepository.getConsumedProteins(username);
        int totalFats = userRepository.getConsumedFats(username);
        int totalCarbohydrates = userRepository.getConsumedCarbohydrates(username);

        proteinProgressBar.setValue(totalProteins);
        fatProgressBar.setValue(totalFats);
        carbohydrateProgressBar.setValue(totalCarbohydrates);

        int maxProteins = proteinProgressBar.getMaximum();
        int maxFats = fatProgressBar.getMaximum();
        int maxCarbohydrates = carbohydrateProgressBar.getMaximum();

        proteinLabel.setText(totalProteins + "g / " + maxProteins + "g");
        fatLabel.setText(totalFats + "g / " + maxFats + "g");
        carbohydrateLabel.setText(totalCarbohydrates + "g / " + maxCarbohydrates + "g");
    }

    private void updateMealLog() {
        ResultSet rs = userRepository.getMeals(username);
        mealLogArea.setText("");
        int totalCalories = 0;
        int totalProteins = 0;
        int totalFats = 0;
        int totalCarbohydrates = 0;
        try {
            while (rs.next()) {
                String mealName = rs.getString("meal_name");
                int calories = rs.getInt("calories");
                int proteins = rs.getInt("proteins");
                int fats = rs.getInt("fats");
                int carbohydrates = rs.getInt("carbohydrates");
                mealLogArea.append(mealName + ": " + calories + " kcal, " + proteins + "g proteins, " + fats + "g fats, " + carbohydrates + "g carbohydrates\n");
                totalCalories += calories;
                totalProteins += proteins;
                totalFats += fats;
                totalCarbohydrates += carbohydrates;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateConsumedMacros();
    }

    private void resetMacros() {
        userRepository.resetConsumedMacros(username);
        updateConsumedCalories();
        mainPage.updateCalorieProgressBar();
        updateConsumedMacros();
        updateMealLog();
    }

    private void updateCalories() {
        try {
            int proteins = Integer.parseInt(mealProteinsField.getText());
            int fats = Integer.parseInt(mealFatsField.getText());
            int carbohydrates = Integer.parseInt(mealCarbohydratesField.getText());
            int calories = (proteins * 4) + (fats * 9) + (carbohydrates * 4);
            mealCaloriesField.setText(String.valueOf(calories));
        } catch (NumberFormatException e) {
            mealCaloriesField.setText("");
        }
    }

    private class MacroFieldListener implements javax.swing.event.DocumentListener {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            updateCalories();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            updateCalories();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            updateCalories();
        }
    }
    public void refreshFoodComboBox() {
        presetFoodComboBox.removeAllItems();
        try {
            List<Map<String, Object>> presetFoods = userRepository.getPresetFoods();
            for (Map<String, Object> food : presetFoods) {
                presetFoodComboBox.addItem((String) food.get("food_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
