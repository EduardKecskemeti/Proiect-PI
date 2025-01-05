package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorkoutProgressPage extends JFrame {

    public WorkoutProgressPage() {
        setTitle("Workout Progress");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);

        // Initialize components
        JLabel label = new JLabel("Workout Progress Page");
        JButton addWorkoutButton = new JButton("Add Workout");

        // Add action listener to the button
        addWorkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddWorkoutPage();
            }
        });

        // Layout setup
        setLayout(new BorderLayout());
        add(label, BorderLayout.NORTH);
        add(addWorkoutButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}