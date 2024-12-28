package ui;

import database.UserRepository;

import javax.swing.*;
import java.awt.*;

public class UserDetailsPage extends JFrame {

    private JTextField weightField;
    private JTextField ageField;
    private JTextField heightField;
    private JComboBox<String> genderComboBox;
    private JButton submitButton;
    private UserRepository userRepository;
    private String username;

    public UserDetailsPage(String username, UserRepository userRepository) {
        this.username = username;
        this.userRepository = userRepository;

        setTitle("User Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));
        setResizable(false);

        // Initialize components
        weightField = new JTextField(10);
        ageField = new JTextField(10);
        heightField = new JTextField(10);
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        submitButton = new JButton("Submit");

        // Layout setup
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Weight (kg):"));
        panel.add(weightField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Height (cm):"));
        panel.add(heightField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderComboBox);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(submitButton);

        // Add panel to frame
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Add action listener to submit button
        submitButton.addActionListener(e -> submitUserDetails());
    }

    private void submitUserDetails() {
        // Code to handle user details submission
        int weight = Integer.parseInt(weightField.getText());
        int age = Integer.parseInt(ageField.getText());
        int height = Integer.parseInt(heightField.getText());
        String gender = (String) genderComboBox.getSelectedItem();

        if (userRepository.saveUserDetails(username, weight, age, height, gender)) {
            JOptionPane.showMessageDialog(this, "Details submitted successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit details.");
        }
    }
}