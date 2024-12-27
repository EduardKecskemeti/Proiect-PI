package ui;

import javax.swing.*;
import java.awt.*;

public class WorkoutForm extends JFrame {
    public WorkoutForm() {
        setTitle("Workout Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panoul principal
        JPanel panel = new JPanel(new GridLayout(4, 2));

        // Adaugă etichete și câmpuri text
        panel.add(new JLabel("Exercițiu:"));
        JTextField exerciseField = new JTextField();
        panel.add(exerciseField);

        panel.add(new JLabel("Seturi:"));
        JTextField setsField = new JTextField();
        panel.add(setsField);

        panel.add(new JLabel("Repetări:"));
        JTextField repsField = new JTextField();
        panel.add(repsField);

        panel.add(new JLabel("Greutate:"));
        JTextField weightField = new JTextField();
        panel.add(weightField);

        // Adaugă butoane
        JButton saveButton = new JButton("Salvează");
        saveButton.addActionListener(e -> {
            String exercise = exerciseField.getText();
            String sets = setsField.getText();
            String reps = repsField.getText();
            String weight = weightField.getText();
            System.out.println("Exercițiu: " + exercise + ", Seturi: " + sets + ", Repetări: " + reps + ", Greutate: " + weight);
        });

        JButton clearButton = new JButton("Resetează");
        clearButton.addActionListener(e -> {
            exerciseField.setText("");
            setsField.setText("");
            repsField.setText("");
            weightField.setText("");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WorkoutForm::new);
    }
}
