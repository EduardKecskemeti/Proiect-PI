// src/main/java/ui/WorkoutProgressPage.java
package ui;

import database.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkoutProgressPage extends JFrame {

    private UserRepository userRepository;
    private String username;

    public WorkoutProgressPage(String username) {
        this.username = username;
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
        addWorkoutButton.addActionListener(e -> {
            new AddWorkoutPage(username);
            dispose();
        });

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
            int workoutId = userRepository.getLastWorkoutId(username);
            if (workoutId != -1) {
                JLabel dateLabel = new JLabel("Last Workout ID: " + workoutId);
                workoutPanel.add(dateLabel);

                System.out.println("Last Workout ID: " + workoutId); // Debug statement

                ResultSet muscleGroups = userRepository.getMuscleGroupsByWorkoutId(workoutId);
                while (muscleGroups.next()) {
                    String muscleGroup = muscleGroups.getString("muscle_group");
                    int sets = muscleGroups.getInt("sets");
                    JLabel muscleGroupLabel = new JLabel("Muscle Group: " + muscleGroup + " (Sets: " + sets + ")");
                    workoutPanel.add(muscleGroupLabel);

                    System.out.println("Muscle Group: " + muscleGroup + " (Sets: " + sets + ")"); // Debug statement

                    ResultSet exercises = userRepository.getExercisesByMuscleGroup(muscleGroup);
                    while (exercises.next()) {
                        String exerciseName = exercises.getString("exercise_name");
                        int exerciseSets = exercises.getInt("sets");
                        JLabel exerciseLabel = new JLabel("  Exercise: " + exerciseName + " (Sets: " + exerciseSets + ")");
                        workoutPanel.add(exerciseLabel);

                        System.out.println("  Exercise: " + exerciseName + " (Sets: " + exerciseSets + ")"); // Debug statement

                        ResultSet bestSet = userRepository.getBestSetByExercise(exerciseName);
                        if (bestSet.next()) {
                            int weight = bestSet.getInt("weight");
                            int reps = bestSet.getInt("reps");
                            JLabel bestSetLabel = new JLabel("    Best Set: " + weight + " kg x " + reps + " reps");
                            workoutPanel.add(bestSetLabel);

                            System.out.println("    Best Set: " + weight + " kg x " + reps + " reps"); // Debug statement
                        }
                    }
                }
            } else {
                JLabel noWorkoutsLabel = new JLabel("No workouts registered");
                workoutPanel.add(noWorkoutsLabel);
                System.out.println("No workouts registered"); // Debug statement
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(new JScrollPane(workoutPanel), BorderLayout.CENTER);
    }
}