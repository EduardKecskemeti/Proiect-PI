package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WorkoutSuggestionsPage extends JFrame {

    public WorkoutSuggestionsPage() {
        setTitle("Workout Suggestions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(900, 700));
        setResizable(true);

        // Initialize components
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        mainPanel.add(createWorkoutPanel("Upper Lower", "resources/upper_lower.jpeg"));
        mainPanel.add(createWorkoutPanel("Push Pull Legs", "resources/push_pull_legs.jpg"));
        mainPanel.add(createWorkoutPanel("Bro Split", "resources/bro_split.png"));
        mainPanel.add(createWorkoutPanel("Full Body", "resources/full_body.jpg"));

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createWorkoutPanel(String workoutName, String imagePath) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(workoutName, SwingConstants.CENTER);
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showWorkoutDetails(workoutName);
            }
        });

        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(label, BorderLayout.SOUTH);
        return panel;
    }

    private void showWorkoutDetails(String workoutName) {
        JFrame detailsFrame = new JFrame(workoutName + " Details");
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailsFrame.setPreferredSize(new Dimension(600, 400));
        detailsFrame.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(workoutName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JTextPane detailsPane = new JTextPane();
        detailsPane.setContentType("text/html");
        detailsPane.setText(getWorkoutDetails(workoutName));
        detailsPane.setEditable(false);
        detailsPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(detailsPane);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        detailsFrame.add(mainPanel);
        detailsFrame.pack();
        detailsFrame.setLocationRelativeTo(null);
        detailsFrame.setVisible(true);
    }

    private String getWorkoutDetails(String workoutName) {
        switch (workoutName) {
            case "Upper Lower":
                return "<html><h2>Upper Lower</h2>" +
                        "<p>The Upper Lower split involves alternating between upper body and lower body workouts.</p>" +
                        "<h3>Pros:</h3>" +
                        "<ul><li>Balanced training</li><li>Good for strength and hypertrophy</li></ul>" +
                        "<h3>Cons:</h3>" +
                        "<ul><li>Requires more gym days</li></ul>" +
                        "<h3>Sample Routine:</h3>" +
                        "<ul><li>Day 1: Upper (Bench Press - 4 sets of 8 reps, Rows - 3 sets of 10 reps, Shoulder Press - 3 sets of 12 reps, Bicep Curls - 3 sets of 15 reps, Tricep Extensions - 3 sets of 15 reps)</li>" +
                        "<li>Day 2: Lower (Squats - 4 sets of 8 reps, Deadlifts - 3 sets of 10 reps, Leg Press - 3 sets of 12 reps, Calf Raises - 3 sets of 20 reps, Hamstring Curls - 3 sets of 15 reps)</li>" +
                        "<li>Day 3: Rest</li>" +
                        "<li>Day 4: Upper (Incline Bench - 3 sets of 8 reps, Pull-Ups - 3 sets of 10 reps, Lateral Raises - 3 sets of 15 reps, Hammer Curls - 3 sets of 12 reps, Skull Crushers - 3 sets of 12 reps)</li>" +
                        "<li>Day 5: Lower (Lunges - 3 sets of 12 reps, Leg Curls - 3 sets of 15 reps, Glute Bridges - 3 sets of 20 reps, Hack Squats - 3 sets of 10 reps, Standing Calf Raises - 3 sets of 20 reps)</li>" +
                        "<li>Day 6: Rest</li><li>Day 7: Rest</li></ul></html>";
            case "Push Pull Legs":
                return "<html><h2>Push Pull Legs</h2>" +
                        "<p>The Push Pull Legs split divides workouts into pushing, pulling, and leg exercises.</p>" +
                        "<h3>Pros:</h3>" +
                        "<ul><li>Efficient muscle group targeting</li><li>Good for hypertrophy</li></ul>" +
                        "<h3>Cons:</h3>" +
                        "<ul><li>Requires more gym days</li></ul>" +
                        "<h3>Sample Routine:</h3>" +
                        "<ul><li>Day 1: Push (Bench Press - 4 sets of 8 reps, Shoulder Press - 3 sets of 10 reps, Tricep Extensions - 3 sets of 12 reps, Lateral Raises - 3 sets of 15 reps, Dips - 3 sets of 15 reps)</li>" +
                        "<li>Day 2: Pull (Deadlifts - 3 sets of 6 reps, Rows - 3 sets of 10 reps, Lat Pulldowns - 3 sets of 12 reps, Bicep Curls - 3 sets of 12 reps, Face Pulls - 3 sets of 15 reps)</li>" +
                        "<li>Day 3: Legs (Squats - 4 sets of 8 reps, Leg Press - 3 sets of 12 reps, Calf Raises - 3 sets of 20 reps, Hamstring Curls - 3 sets of 15 reps, Lunges - 3 sets of 12 reps)</li>" +
                        "<li>Day 4: Rest</li>" +
                        "<li>Day 5: Push (Incline Bench - 3 sets of 8 reps, Dumbbell Shoulder Press - 3 sets of 10 reps, Tricep Pushdowns - 3 sets of 12 reps, Pec Deck - 3 sets of 12 reps, Push-Ups - 3 sets of 20 reps)</li>" +
                        "<li>Day 6: Pull (Pull-Ups - 3 sets of 10 reps, T-Bar Rows - 3 sets of 10 reps, Seated Rows - 3 sets of 12 reps, Hammer Curls - 3 sets of 12 reps, Preacher Curls - 3 sets of 12 reps)</li>" +
                        "<li>Day 7: Legs (Lunges - 3 sets of 12 reps, Hamstring Curls - 3 sets of 15 reps, Glute Bridges - 3 sets of 20 reps, Hack Squats - 3 sets of 10 reps, Standing Calf Raises - 3 sets of 20 reps)</li></ul></html>";
            case "Bro Split":
                return "<html><h2>Bro Split</h2>" +
                        "<p>The Bro Split is a traditional bodybuilding routine where each workout focuses on a single muscle group.</p>" +
                        "<h3>Pros:</h3>" +
                        "<ul><li>Allows for high volume training</li><li>Focuses on individual muscle groups</li></ul>" +
                        "<h3>Cons:</h3>" +
                        "<ul><li>Requires more gym days</li></ul>" +
                        "<h3>Sample Routine:</h3>" +
                        "<ul><li>Day 1: Chest (Bench Press - 4 sets of 8 reps, Incline Bench - 3 sets of 10 reps, Chest Flies - 3 sets of 12 reps, Cable Crossovers - 3 sets of 15 reps, Push-Ups - 3 sets of 20 reps)</li>" +
                        "<li>Day 2: Back (Deadlifts - 3 sets of 6 reps, Rows - 3 sets of 10 reps, Lat Pulldowns - 3 sets of 12 reps, Pullovers - 3 sets of 15 reps, Pull-Ups - 3 sets of 10 reps)</li>" +
                        "<li>Day 3: Shoulders (Shoulder Press - 3 sets of 10 reps, Lateral Raises - 3 sets of 15 reps, Face Pulls - 3 sets of 15 reps, Upright Rows - 3 sets of 12 reps, Reverse Flies - 3 sets of 15 reps)</li>" +
                        "<li>Day 4: Arms (Bicep Curls - 3 sets of 12 reps, Tricep Extensions - 3 sets of 12 reps, Hammer Curls - 3 sets of 12 reps, Skull Crushers - 3 sets of 12 reps, Preacher Curls - 3 sets of 12 reps, Dips - 3 sets of 15 reps)</li>" +
                        "<li>Day 5: Legs (Squats - 4 sets of 8 reps, Leg Press - 3 sets of 12 reps, Calf Raises - 3 sets of 20 reps, Hamstring Curls - 3 sets of 15 reps, Lunges - 3 sets of 12 reps, Leg Extensions - 3 sets of 15 reps)</li>" +
                        "<li>Day 6: Rest</li><li>Day 7: Rest</li></ul></html>";
            case "Full Body":
                return "<html><h2>Full Body</h2>" +
                        "<p>The Full Body split involves training all major muscle groups in a single workout session.</p>" +
                        "<h3>Pros:</h3>" +
                        "<ul><li>Great for beginners</li><li>Not time-consuming</li><li>Allows for optimal recovery</li></ul>" +
                        "<h3>Cons:</h3>" +
                        "<ul><li>May not be the best for advanced lifters</li><li>Not optimal for hypertrophy</li></ul>" +
                        "<h3>Sample Routine:</h3>" +
                        "<ul><li>Day 1: Full Body (Bench Press - 3 sets of 8 reps, Squats - 3 sets of 10 reps, Rows - 3 sets of 12 reps, Shoulder Press - 3 sets of 10 reps, Bicep Curls - 3 sets of 12 reps, Tricep Extensions - 3 sets of 12 reps)</li>" +
                        "<li>Day 2: Rest</li>" +
                        "<li>Day 3: Full Body (Deadlifts - 3 sets of 6 reps, Pull-Ups - 3 sets of 10 reps, Lunges - 3 sets of 12 reps, Bicep Curls - 3 sets of 12 reps, Face Pulls - 3 sets of 15 reps, Tricep Pushdowns - 3 sets of 12 reps)</li>" +
                        "<li>Day 4: Rest</li>" +
                        "<li>Day 5: Full Body (Incline Bench - 3 sets of 8 reps, Leg Press - 3 sets of 12 reps, Face Pulls - 3 sets of 15 reps, Tricep Extensions - 3 sets of 12 reps, Calf Raises - 3 sets of 20 reps, Lateral Raises - 3 sets of 15 reps)</li>" +
                        "<li>Day 6: Rest</li><li>Day 7: Rest</li></ul></html>";
            default:
                return "<html><p>No details available.</p></html>";
        }
    }
}