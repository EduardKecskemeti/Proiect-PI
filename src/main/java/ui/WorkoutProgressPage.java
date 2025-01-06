package ui;

import database.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkoutProgressPage extends JFrame {

    private UserRepository userRepository;

    public WorkoutProgressPage() {
        try {
            userRepository = new UserRepository();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setTitle("Workout Progress");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);

        // Initialize components
        JLabel label = new JLabel("Workout Progress Page");
        JButton addWorkoutButton = new JButton("Add Workout");

        // Add action listener to the button
        addWorkoutButton.addActionListener(e -> new AddWorkoutPage());

        // Layout setup
        setLayout(new BorderLayout());
        add(label, BorderLayout.NORTH);
        add(addWorkoutButton, BorderLayout.SOUTH);

        // Fetch and display the last workout
        displayLastWorkout();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void displayLastWorkout() {
        JPanel workoutPanel = new JPanel();
        workoutPanel.setLayout(new BoxLayout(workoutPanel, BoxLayout.Y_AXIS));

        try {
            ResultSet lastWorkout = userRepository.getLastWorkout();
            if (lastWorkout.next()) {
                String date = lastWorkout.getString("date");
                JLabel dateLabel = new JLabel("Last Workout Date: " + date);
                workoutPanel.add(dateLabel);

                ResultSet muscleGroups = userRepository.getMuscleGroupsByWorkoutDate(date);
                while (muscleGroups.next()) {
                    String muscleGroup = muscleGroups.getString("muscle_group");
                    int sets = muscleGroups.getInt("sets");
                    JLabel muscleGroupLabel = new JLabel("Muscle Group: " + muscleGroup + " (Sets: " + sets + ")");
                    workoutPanel.add(muscleGroupLabel);

                    ResultSet exercises = userRepository.getExercisesByMuscleGroup(muscleGroup);

                    while (exercises.next()) {
                        String exerciseName = exercises.getString("exercise_name");
                        int exerciseSets = exercises.getInt("sets");
                        JLabel exerciseLabel = new JLabel("  Exercise: " + exerciseName + " (Sets: " + exerciseSets + ")");
                        workoutPanel.add(exerciseLabel);

                        ResultSet bestSet = userRepository.getBestSetByExercise(exerciseName);
                        if (bestSet.next()) {
                            int weight = bestSet.getInt("weight");
                            int reps = bestSet.getInt("reps");
                            JLabel bestSetLabel = new JLabel("    Best Set: " + weight + " kg x " + reps + " reps");
                            workoutPanel.add(bestSetLabel);
                        }
                    }
                }
            } else {
                JLabel noWorkoutsLabel = new JLabel("No workouts registered");
                workoutPanel.add(noWorkoutsLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(new JScrollPane(workoutPanel), BorderLayout.CENTER);
    }
}