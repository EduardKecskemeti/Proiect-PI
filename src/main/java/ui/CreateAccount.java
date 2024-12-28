package ui;

import database.UserRepository;

import javax.swing.*;
import java.awt.*;

public class CreateAccount extends JFrame {

    private JTextField UserText;
    private JPasswordField passwordField1;
    private JButton createAccountButton;
    private JPanel Jpanel;
    private JFrame frame;
    private UserRepository userRepository;

    public CreateAccount(UserRepository userRepository) {
        this.userRepository = userRepository;

        frame = new JFrame("Create Account");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.setResizable(false);

        // Initialize components
        UserText = new JTextField(20);
        passwordField1 = new JPasswordField(20);
        createAccountButton = new JButton("Create Account");
        Jpanel = new JPanel(new GridLayout(3, 2));

        // Add components to panel
        Jpanel.add(new JLabel("Username:"));
        Jpanel.add(UserText);
        Jpanel.add(new JLabel("Password:"));
        Jpanel.add(passwordField1);
        Jpanel.add(new JLabel()); // Empty label for spacing
        Jpanel.add(createAccountButton);

        // Add panel to frame
        frame.add(Jpanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listener to create account button
        createAccountButton.addActionListener(e -> {
            String username = UserText.getText();
            String password = new String(passwordField1.getPassword());
            if (userRepository.addUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Account created successfully!");
                frame.dispose();
                new UserDetailsPage(username, userRepository);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to create account.");
            }
        });
    }
}