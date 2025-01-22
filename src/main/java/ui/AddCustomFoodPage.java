// src/main/java/ui/AddCustomFoodPage.java
package ui;

import database.UserRepository;

import javax.swing.*;
import java.awt.*;

public class AddCustomFoodPage extends JFrame {
    private JTextField foodNameField;
    private JTextField caloriesField;
    private JTextField proteinsField;
    private JTextField fatsField;
    private JTextField carbohydratesField;
    private JButton addButton;
    private UserRepository userRepository;
    private MacroTrackerPage macroTrackerPage;

    public AddCustomFoodPage(UserRepository userRepository, MacroTrackerPage macroTrackerPage) {
        this.userRepository = userRepository;
        this.macroTrackerPage = macroTrackerPage;

        setTitle("Add Custom Food");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));
        setResizable(false);

        // Initialize components
        foodNameField = new JTextField(20);
        caloriesField = new JTextField(10);
        proteinsField = new JTextField(10);
        fatsField = new JTextField(10);
        carbohydratesField = new JTextField(10);
        addButton = new JButton("Add Food");

        // Layout setup
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Food Name:"));
        panel.add(foodNameField);
        panel.add(new JLabel("Calories:"));
        panel.add(caloriesField);
        panel.add(new JLabel("Proteins:"));
        panel.add(proteinsField);
        panel.add(new JLabel("Fats:"));
        panel.add(fatsField);
        panel.add(new JLabel("Carbohydrates:"));
        panel.add(carbohydratesField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(addButton);

        // Add panel to frame
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Add action listener to add button
        addButton.addActionListener(e -> addCustomFood());
    }

    private void addCustomFood() {
        String foodName = foodNameField.getText();
        int calories = Integer.parseInt(caloriesField.getText());
        int proteins = Integer.parseInt(proteinsField.getText());
        int fats = Integer.parseInt(fatsField.getText());
        int carbohydrates = Integer.parseInt(carbohydratesField.getText());

        if (userRepository.addCustomFood(foodName, calories, proteins, fats, carbohydrates)) {
            JOptionPane.showMessageDialog(this, "Food added successfully!");
            macroTrackerPage.refreshFoodComboBox();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add food.");
        }
    }
}