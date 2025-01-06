package ui;

import database.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Login extends JFrame {

    private JTextField UserText;
    private JPanel panel1;
    private JPasswordField passwordField1;
    private JButton loginbutton;
    private JButton createAccountButton;
    private JPanel Jpanel;
    private JFrame frame;
    private UserRepository userRepository;

    public Login() {
        try {
            userRepository = new UserRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        frame = new JFrame("Login Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.setResizable(false);

        // Initialize components
        UserText = new JTextField(20);
        passwordField1 = new JPasswordField(20);
        loginbutton = new JButton("Login");
        createAccountButton = new JButton("Create Account");
        Jpanel = new JPanel(new GridLayout(4, 2));

        // Add components to panel
        Jpanel.add(new JLabel("Username:"));
        Jpanel.add(UserText);
        Jpanel.add(new JLabel("Password:"));
        Jpanel.add(passwordField1);
        Jpanel.add(new JLabel()); // Empty label for spacing
        Jpanel.add(loginbutton);
        Jpanel.add(new JLabel()); // Empty label for spacing
        Jpanel.add(createAccountButton);

        // Add panel to frame
        frame.add(Jpanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listener to login button
        loginbutton.addActionListener(e -> {
            String username = UserText.getText();
            String password = new String(passwordField1.getPassword());
            if (userRepository.validateUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                frame.dispose();
                new MainPage(username, userRepository);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.");
            }
        });

        // Add action listener to create account button
        createAccountButton.addActionListener(e -> {
            new CreateAccount(userRepository);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}