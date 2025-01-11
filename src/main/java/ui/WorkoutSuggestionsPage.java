package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WorkoutSuggestionsPage extends JFrame {

    public WorkoutSuggestionsPage() {
        setTitle("Workout Suggestions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
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

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setText(getWorkoutDetails(workoutName));

        detailsFrame.add(new JScrollPane(detailsArea));
        detailsFrame.pack();
        detailsFrame.setLocationRelativeTo(null);
        detailsFrame.setVisible(true);
    }

    private String getWorkoutDetails(String workoutName) {
        switch (workoutName) {
            case "Upper Lower":
                return "The Upper Lower split involves dividing your workouts into upper body and lower body days. " +"\n" +
                        "This allows for balanced training and recovery. Typically, you will train 4 days a week, " +"\n" +
                        "alternating between upper and lower body exercises."+ "\n" +"" +
                        "Pros:"+"\n"+" Offers a lot of flexibility when it comes to choosing exercises " +"\n" +
                        "Allows for great recovery between workouts " +"\n" +
                        "One of the most effective splits for muscle growth and strength gains"+ "\n" +
                        "Cons:" +"\n" +"Can be time-consuming " +"\n" +
                        "Upper body days can be intense and require a lot of energy " +"\n" +
                        "May not be suitable for beginners or those with limited time"+ "\n" +
                        "Sample Upper Lower Split Routine:" +"\n" +
                        "Day 1: Upper Body" +"\n" +
                        "Chest press: 2 sets of 4-6 reps" +"\n" +
                        "Wide grip lat Pulldown: 2 sets of 8-10 reps" +"\n" +
                        "Barbell row: 2 sets of 6-8 reps" +"\n" +
                        "Lateral raises: 3 sets of 12-15 reps" +"\n" +
                        "Dips: 3 sets of 8-10 reps" +"\n" +
                        "Preacher curls: 3 sets of 10-12 reps" +"\n" +
                        "Day 2: Lower Body" +"\n" +
                        "Hamstring curls: 4 sets of 10-12 reps" +"\n" +
                        "Squats: 3 sets of 4-6 reps" +"\n" +
                        "Leg extensions: 3 sets of 12-15 reps" +"\n" +
                        "Toe Press: 3 sets of 15-20 reps" +"\n" +
                        "Day 3: Rest" +"\n" +
                        "Day 4: Upper Body 2" +"\n" +
                        "Incine bench press: 3 sets of 6-8 reps" +"\n" +
                        "Cable flies: 3 sets of 10-12 reps" +"\n" +
                        "Seated rows: 2 sets of 8-10 reps" +"\n" +
                        "Close grip pulldowns: 2 sets of 10-12 reps" +"\n" +
                        "Dumbbell curls: 2 sets of 12-15 reps" +"\n" +
                        "Tricep pushdowns: 3 sets of 8-10 reps" +"\n" +
                        "Rear delt flies: 3 sets of 12-15 reps" +"\n" +
                        "Day 5: Lower Body 2" +"\n" +
                        "Seated leg curls: 2 sets of 4-8 reps" +"\n" +
                        "Romanian deadlifts: 3 sets of 6-8 reps" +"\n" +
                        "Leg press: 3 sets of 8-10 reps" +"\n" +
                        "Calf raises: 3 sets of 12-15 reps" +"\n" +
                        "Day 6: Rest" +"\n" +
                        "Day 7: Rest";
            case "Push Pull Legs":
                return "The Push Pull Legs split is a popular and effective workout routine that divides your workouts " +"\n" +
                        "into three categories: push (chest, shoulders, triceps), pull (back, biceps), and legs (quads, " +"\n" +
                        "hamstrings, calves). This split allows for optimal recovery and muscle growth."
                        +"\n" +
                        "Pros:" +"\n" +
                        "Balanced training for all major muscle groups " +"\n" +
                        "Allows for great recovery between workouts " +"\n" +
                        "Good for intermediate lifters " +"\n" +
                        "Cons:" +"\n" +
                        "Not the best for hypertrophy " +"\n" +
                        "Usually your biceps and triceps will be tired, so your growth for arms might be limited" +"\n" +
                        "Sample Push Pull Legs Routine:" +"\n" +
                        "Day 1: Push" +"\n" +
                        "Bench press: 3 sets of 6-8 reps" +"\n" +
                        "Chest flies: 3 sets of 8-10 reps" +"\n" +
                        "Overhead press: 3 sets of 6-8 reps" +"\n" +
                        "Lateral raises: 3 sets of 10-12 reps" +"\n" +
                        "Tricep dips: 3 sets of 8-10 reps" +"\n" +
                        "Cable tricep pushdowns: 3 sets of 10-12 reps" +"\n" +
                        "Day 2: Pull" +"\n" +
                        "Pull-ups: 3 sets of 6-8 reps" +"\n" +
                        "Cable pullovers 3 sets of 8-10 reps" +"\n" +
                        "Barbell rows: 3 sets of 6-8 reps" +"\n" +
                        "Machine rows: 3 sets of 10-12 reps" +"\n" +
                        "Bicep curls: 3 sets of 8-10 reps" +"\n" +
                        "Hammer curls: 3 sets of 10-12 reps" +"\n" +
                        "Rear delt flies: 3 sets of 12-15 reps" +"\n" +
                        "Day 3: Legs" +"\n" +
                        "Squats: 3 sets of 6-8 reps" +"\n" +
                        "Leg press: 3 sets of 8-10 reps" +"\n" +
                        "Leg extensions: 3 sets of 10-12 reps" +"\n" +
                        "Leg curls: 3 sets of 10-12 reps" +"\n" +
                        "Lying leg curls: 3 sets of 12-15 reps" +"\n" +
                        "Calf raises: 3 sets of 12-15 reps" +"\n" ;

            case "Bro Split":
                return "The Bro Split is a traditional bodybuilding routine where each workout focuses on a single muscle " +"\n" +
                        "group. Typically, you will train 5-6 days a week, with each day dedicated to a different muscle " +"\n" +
                        "group such as chest, back, shoulders, arms, and legs." +"\n" +
                        "Pros:" +"\n" +
                        "Allows for maximum muscle isolation and focus " +"\n" +
                        "By far the most fun workout split " +"\n" +
                        "Cons:" +"\n" +
                        "Not the most efficient way to train " +"\n" +
                        "Can lead to overtraining and burnout " +"\n" +
                        "Your muscles don't benefit from that much volume in a day" +"\n" +
                        "Sample Bro Split Routine:" +"\n" +
                        "Day 1: Chest" +"\n" +
                        "Bench press: 4 sets of 6-8 reps" +"\n" +
                        "Incline bench press: 4 sets of 8-10 reps" +"\n" +
                        "Chest flies: 3 sets of 10-12 reps" +"\n" +
                        "Day 2: Back" +"\n" +
                        "Pull-ups: 4 sets of 6-8 reps" +"\n" +
                        "Barbell rows: 4 sets of 8-10 reps" +"\n" +
                        "Lat pulldowns: 3 sets of 10-12 reps" +"\n" +
                        "Day 3: Shoulders" +"\n" +
                        "Overhead press: 4 sets of 6-8 reps" +"\n" +
                        "Lateral raises: 4 sets of 8-10 reps" +"\n" +
                        "Rear delt flies: 3 sets of 10-12 reps" +"\n" +
                        "Day 4: Arms" +"\n" +
                        "Bicep curls: 4 sets of 6-8 reps" +"\n" +
                        "Preacher curls: 4 sets of 8-10 reps" +"\n" +
                        "Hammer curls: 3 sets of 10-12 reps" +"\n" +
                        "Tricep dips: 4 sets of 6-8 reps" +"\n" +
                        "Tricep pushdowns: 4 sets of 8-10 reps" +"\n" +
                        "Skull crushers: 3 sets of 10-12 reps" +"\n" +
                        "Day 5: Legs" +"\n" +
                        "Squats: 4 sets of 6-8 reps" +"\n" +
                        "Leg press: 4 sets of 8-10 reps" +"\n" +
                        "Leg extensions: 3 sets of 10-12 reps" +"\n" +
                        "Leg curls: 3 sets of 10-12 reps" +"\n" +
                        "Calf raises: 4 sets of 12-15 reps";


            case "Full Body":
                return "The Full Body split involves training all major muscle groups in a single workout session. " +"\n" +
                        "This split is great for beginners and those with limited time, as it allows for efficient and " +"\n" +
                        "effective training. Typically, you will train 3 days a week with rest days in between."
                        +"\n" +
                        "Pros:" +"\n" +
                        "Great for beginners " +"\n" +
                        "Not time-consuming " +"\n" +
                        "Allows for optimal recovery " +"\n" +
                        "Cons:" +"\n" +
                        "May not be the best for advanced lifters " +"\n" +
                        "Not optimal for hypertrophy " +"\n" +
                        "Sample Full Body Routine:" +"\n" +
                        "Day 1: Full Body" +"\n" +
                        "Squats: 3 sets of 6-8 reps" +"\n" +
                        "Bench press: 3 sets of 6-8 reps" +"\n" +
                        "Pull-ups: 3 sets of 6-8 reps" +"\n" +
                        "Overhead press: 3 sets of 6-8 reps" +"\n" +
                        "Leg curls: 3 sets of 10-12 reps" +"\n" +
                        "Calf raises: 3 sets of 12-15 reps" +"\n" +
                        "Day 2: Rest" +"\n" +
                        "Day 3: Rest" +"\n" +
                        "Day 4: Full Body" +"\n" +
                        "Deadlifts: 3 sets of 6-8 reps" +"\n" +
                        "Dips: 3 sets of 6-8 reps" +"\n" +
                        "Barbell rows: 3 sets of 6-8 reps" +"\n" +
                        "Lateral raises: 3 sets of 10-12 reps" +"\n" +
                        "Leg extensions: 3 sets of 10-12 reps" +"\n" +
                        "Day 5: Rest" +"\n" +
                        "Day 6: Rest" +"\n" +
                        "Day 7: Rest";
            default:
                return "No details available.";
        }
    }
}