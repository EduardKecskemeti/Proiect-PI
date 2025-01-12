// src/main/java/ui/AddWorkoutPage.java
package ui;

import database.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AddWorkoutPage extends JFrame {

    private JComboBox<String> muscleComboBox;
    private JTextField dateField;
    private JComboBox<String> exerciseComboBox;
    private JComboBox<String> exerciseTypeComboBox;
    private JTextField weightField;
    private JTextField repsField;
    private JPanel dynamicPanel;
    private JButton newExerciseButton;
    private JButton newSetButton;
    private JButton addMuscleGroupButton;
    private JButton finishWorkoutButton;
    private JLabel r = new JLabel("Reps:");
    private JLabel w = new JLabel("Weight Used:");
    private Map<String, String[]> muscleExercises;
    private UserRepository userRepository;

    private String currentMuscleGroup;
    private String currentExercise;
    private int currentExerciseSets;
    private String username;
    private Map<String, Integer> muscleGroupSets;
    private MainPage mainPage;
    public AddWorkoutPage(String username, MainPage mainPage) {
        this.mainPage = mainPage;
        this.username = username;
        try {
            userRepository = new UserRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setTitle("Add Workout");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 400));
        setResizable(true);

        // Initialize muscle exercises map
        muscleExercises = new HashMap<>();
        muscleExercises.put("Chest", new String[]{"Bench Press", "Incline Bench", "Chest Flies", "Push Ups"});
        muscleExercises.put("Back", new String[]{"Deadlifts", "Lat Pulldowns", "Rows", "Pullovers", "Pullups"});
        muscleExercises.put("Biceps", new String[]{"Bicep Curls", "Hammer Curls", "Preacher Curls", "Behind the Back Curls"});
        muscleExercises.put("Triceps", new String[]{"Tricep Pushdowns", "Tricep Extensions", "Dips", "Overhead Extensions", "JM Press", "Close Grip Bench"});
        muscleExercises.put("Shoulders", new String[]{"Shoulder Press", "Lateral Raises", "Upright Rows", "Reverse Flies", "Front Raises", "Face Pulls"});
        muscleExercises.put("Quads", new String[]{"Squats", "Hack Squats", "Leg Extensions", "Leg Press"});
        muscleExercises.put("Hamstrings", new String[]{"Seated Hamstring Curl", "Lying Hamstring Curl", "Stiff Legged Deadlifts"});
        muscleExercises.put("Glutes", new String[]{"Romanian Deadlifts", "Hip Thrusts", "Glute Kickbacks"});
        muscleExercises.put("Calves", new String[]{"Toe Press", "Standing Calf Raise", "Sitting Calf Raise"});

        muscleGroupSets = new HashMap<>();
        for (String muscleGroup : muscleExercises.keySet()) {
            muscleGroupSets.put(muscleGroup, 0);
        }

        // Initialize components
        JLabel dateLabel = new JLabel("Date:");
        dateField = new JTextField(10);
        dateField.setText(LocalDate.now().toString()); // Set today's date as default

        JLabel muscleLabel = new JLabel("Muscle(s) Worked:");
        muscleComboBox = new JComboBox<>(muscleExercises.keySet().toArray(new String[0]));
        muscleComboBox.addActionListener(new MuscleSelectionListener());

        dynamicPanel = new JPanel();
        dynamicPanel.setLayout(new BoxLayout(dynamicPanel, BoxLayout.Y_AXIS));

        newExerciseButton = new JButton("New Exercise");
        newExerciseButton.addActionListener(new NewExerciseListener());

        newSetButton = new JButton("New Set");
        newSetButton.addActionListener(new NewSetListener());

        addMuscleGroupButton = new JButton("Add Another Muscle Group");
        addMuscleGroupButton.addActionListener(new AddMuscleGroupListener());

        finishWorkoutButton = new JButton("Finish Workout");
        finishWorkoutButton.addActionListener(new FinishWorkoutListener());

        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        topPanel.add(dateLabel);
        topPanel.add(dateField);
        topPanel.add(muscleLabel);
        topPanel.add(muscleComboBox);

        add(topPanel, BorderLayout.NORTH);
        add(dynamicPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newExerciseButton);
        buttonPanel.add(newSetButton);
        buttonPanel.add(addMuscleGroupButton);
        buttonPanel.add(finishWorkoutButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class MuscleSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dynamicPanel.removeAll();
            String selectedMuscle = (String) muscleComboBox.getSelectedItem();
            currentMuscleGroup = selectedMuscle; // Set current muscle group
            exerciseComboBox = new JComboBox<>(muscleExercises.get(selectedMuscle));
            exerciseTypeComboBox = new JComboBox<>(new String[]{"Cable", "Barbell", "Dumbbell", "Machine", "Free Weight"});
            dynamicPanel.add(new JLabel("Exercise Name:"));
            dynamicPanel.add(exerciseComboBox);
            dynamicPanel.add(new JLabel("Exercise Type:"));
            dynamicPanel.add(exerciseTypeComboBox);
            dynamicPanel.revalidate();
            dynamicPanel.repaint();
        }
    }

    private class NewExerciseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveCurrentExercise();
            currentExercise = (String) exerciseComboBox.getSelectedItem(); // Set current exercise
            if (weightField != null && repsField != null) {
                dynamicPanel.remove(weightField);
                dynamicPanel.remove(repsField);
                dynamicPanel.remove(r);
                dynamicPanel.remove(w);
            }
            dynamicPanel.add(w);
            weightField = new JTextField(10);
            dynamicPanel.add(weightField);
            dynamicPanel.add(r);
            repsField = new JTextField(10);
            dynamicPanel.add(repsField);
            weightField.setText(""); // Clear weight field
            repsField.setText(""); // Clear reps field
            dynamicPanel.revalidate();
            dynamicPanel.repaint();
        }
    }

    private class NewSetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentExercise == null || currentExercise.equals("Select Exercise")) {
                JOptionPane.showMessageDialog(AddWorkoutPage.this, "Please select an exercise before adding a set.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (weightField != null && repsField != null) {
                String weightText = weightField.getText();
                String repsText = repsField.getText();

                try {
                    int weight = Integer.parseInt(weightText);
                    int reps = Integer.parseInt(repsText);
                    if (weight <= 0 || reps <= 0) {
                        throw new NumberFormatException();
                    }
                    saveSetData(weight, reps);
                    weightField.setText("");
                    repsField.setText("");
                    currentExerciseSets++;
                    muscleGroupSets.put(currentMuscleGroup, muscleGroupSets.get(currentMuscleGroup) + 1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AddWorkoutPage.this, "Please enter valid positive numbers for weight and reps.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                weightField = new JTextField(10);
                repsField = new JTextField(10);
                dynamicPanel.add(new JLabel("Weight Used:"));
                dynamicPanel.add(weightField);
                dynamicPanel.add(new JLabel("Reps:"));
                dynamicPanel.add(repsField);
                dynamicPanel.revalidate();
                dynamicPanel.repaint();
            }
        }
    }

    private void saveSetData(int weight, int reps) {
        if (currentExercise == null) {
            JOptionPane.showMessageDialog(this, "Please select an exercise before adding a set.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            userRepository.saveMaxWeight(username, currentExercise, weight);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving set data.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void saveCurrentExercise() {
        if (currentExercise != null) {
            try {
                userRepository.saveMuscleGroupSets(username, currentMuscleGroup, currentExerciseSets);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error saving exercise data.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        currentExercise = (String) exerciseComboBox.getSelectedItem();
        currentExerciseSets = 0;
    }

    private class AddMuscleGroupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveCurrentExercise();
            dynamicPanel.removeAll();
            dynamicPanel.add(new JLabel("Muscle(s) Worked:"));
            dynamicPanel.add(muscleComboBox);
            dynamicPanel.revalidate();
            dynamicPanel.repaint();
            mainPage.refreshMuscleGroupTable();
        }
    }

    private class FinishWorkoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveCurrentExercise();
            JOptionPane.showMessageDialog(AddWorkoutPage.this, "Workout data saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            mainPage.refreshMuscleGroupTable();
            dispose();
        }
    }
}