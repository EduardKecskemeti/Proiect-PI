// src/main/java/database/UserRepository.java
package database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private static final String URL = "jdbc:sqlite:db/userdb.db";
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_MS = 100;

    public UserRepository() throws SQLException {

    }

    private void executeUpdateWithRetry(String sql, String... params) throws SQLException {
        int retries = 0;
        while (true) {
            try (Connection connection = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setString(i + 1, params[i]);
                }
                pstmt.executeUpdate();
                return;
            } catch (SQLException e) {
                if (e.getErrorCode() == 5 && retries < MAX_RETRIES) { // SQLITE_BUSY error code is 5
                    retries++;
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SQLException("Interrupted during retry", ie);
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    private ResultSet executeQueryWithRetry(String sql, String... params) throws SQLException {
        int retries = 0;
        while (true) {
            try (Connection connection = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setString(i + 1, params[i]);
                }
                return pstmt.executeQuery();
            } catch (SQLException e) {
                if (e.getErrorCode() == 5 && retries < MAX_RETRIES) {
                    retries++;
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SQLException("Interrupted during retry", ie);
                    }
                } else {
                    throw e;
                }
            }
        }
    }



    public void saveSet(String username, String exerciseName, int weight, int reps) throws SQLException {
        String sql = "INSERT INTO Sets (username, exercise_name, weight, reps) VALUES (?, ?, ?, ?)";
        executeUpdateWithRetry(sql, username, exerciseName, String.valueOf(weight), String.valueOf(reps));
    }

    public void updateBestSet(String exerciseName, int weight, int reps) throws SQLException {
        String sql = "SELECT weight, reps FROM BestSets WHERE exercise_name = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int bestWeight = rs.getInt("weight");
                int bestReps = rs.getInt("reps");
                if (weight > bestWeight || (weight == bestWeight && reps > bestReps)) {
                    String updateSql = "UPDATE BestSets SET weight = ?, reps = ? WHERE exercise_name = ?";
                    executeUpdateWithRetry(updateSql, String.valueOf(weight), String.valueOf(reps), exerciseName);
                }
            } else {
                String insertSql = "INSERT INTO BestSets (exercise_name, weight, reps) VALUES (?, ?, ?)";
                executeUpdateWithRetry(insertSql, exerciseName, String.valueOf(weight), String.valueOf(reps));
            }
        }
    }

    public void saveExercise(String muscleGroup, String exerciseName, int sets) throws SQLException {
        String sql = "INSERT INTO Exercises (muscle_group, exercise_name, sets) VALUES (?, ?, ?)";
        executeUpdateWithRetry(sql, muscleGroup, exerciseName, String.valueOf(sets));
    }

    public int saveMuscleWorked(String muscleName, int sets, int topSetWeight, int workoutId) {
        String sql = "INSERT INTO MusclesWorked (muscle_name, sets, top_set_weight, workout_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, muscleName);
            pstmt.setInt(2, sets);
            pstmt.setInt(3, topSetWeight);
            pstmt.setInt(4, workoutId);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public boolean saveWorkoutDate(String username, String date) {
        String sql = "INSERT INTO Workouts (username, date) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, date);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean addUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean saveUserDetails(String username, int weight, int age, int height, String gender, String activityLevel) {
        String sql = "UPDATE users SET weight = ?, age = ?, height = ?, gender = ?, activity_level = ? WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, weight);
            pstmt.setInt(2, age);
            pstmt.setInt(3, height);
            pstmt.setString(4, gender);
            pstmt.setString(5, activityLevel);
            pstmt.setString(6, username);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ResultSet getUserDetails(String username) {
        String sql = "SELECT weight, age, height, gender FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public double calculateDailyCalorieIntake(String username) {
        String sql = "SELECT weight, age, height, gender, activity_level FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int weight = rs.getInt("weight");
                    int age = rs.getInt("age");
                    int height = rs.getInt("height");
                    String gender = rs.getString("gender");
                    String activityLevel = rs.getString("activity_level");

                    double bmr;
                    if (gender.equalsIgnoreCase("Male")) {
                        bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
                    } else if (gender.equalsIgnoreCase("Female")) {
                        bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
                    } else {
                        return 0;
                    }

                    double activityMultiplier;
                    switch (activityLevel) {
                        case "Sedentary (little to no exercise)":
                            activityMultiplier = 1.2;
                            break;
                        case "Lightly active (light exercise 1-3 times a week)":
                            activityMultiplier = 1.375;
                            break;
                        case "Moderately active (moderate exercise/sports 3-5 times a week)":
                            activityMultiplier = 1.55;
                            break;
                        case "Very active (hard exercise/sports 6-7 days a week)":
                            activityMultiplier = 1.725;
                            break;
                        case "Extra active (very hard exercise/sports & physical job or 2x training)":
                            activityMultiplier = 1.9;
                            break;
                        default:
                            activityMultiplier = 1.0;
                            break;
                    }

                    return bmr * activityMultiplier;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public boolean logConsumedCalories(String username, int calories) {
        String sql = "INSERT INTO consumed_calories(username, calories, date) VALUES(?, ?, date('now'))";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, calories);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int getConsumedCalories(String username) {
        String sql = "SELECT SUM(calories) AS total FROM consumed_calories WHERE username = ? AND date = date('now')";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getConsumedProteins(String username) {
        String sql = "SELECT SUM(proteins) AS total FROM meals WHERE username = ? AND date = date('now')";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getConsumedFats(String username) {
        String sql = "SELECT SUM(fats) AS total FROM meals WHERE username = ? AND date = date('now')";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getConsumedCarbohydrates(String username) {
        String sql = "SELECT SUM(carbohydrates) AS total FROM meals WHERE username = ? AND date = date('now')";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public boolean logMeal(String username, String mealName, int calories, int proteins, int fats, int carbohydrates, int amount) {
        String sql = "INSERT INTO meals(username, meal_name, calories, proteins, fats, carbohydrates, amount, date) VALUES(?, ?, ?, ?, ?, ?, ?, date('now'))";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, mealName);
            pstmt.setInt(3, calories);
            pstmt.setInt(4, proteins);
            pstmt.setInt(5, fats);
            pstmt.setInt(6, carbohydrates);
            pstmt.setInt(7, amount);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void resetConsumedMacros(String username) {
        String sql = "DELETE FROM consumed_calories WHERE username = ? AND date = date('now')";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "DELETE FROM meals WHERE username = ? AND date = date('now')";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet getMeals(String username) {
        String sql = "SELECT meal_name, calories, proteins, fats, carbohydrates, date FROM meals WHERE username = ? AND date = date('now')";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ResultSet getTopSetWeightByMuscleGroup(String muscleGroup) throws SQLException {
        String sql = "SELECT MAX(weight) AS weight FROM Sets WHERE exercise_name IN (SELECT exercise_name FROM Exercises WHERE muscle_group = ?)";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, muscleGroup);
            return pstmt.executeQuery();
        }
    }



    public boolean saveExerciseDone(int muscleId, String exerciseName) {
        String sql = "INSERT INTO ExercisesDone (muscle_id, exercise_name) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, muscleId);
            pstmt.setString(2, exerciseName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int getLastWorkoutId(String username) throws SQLException {
        String sql = "SELECT id FROM Workouts WHERE username = ? ORDER BY date DESC LIMIT 1";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int workoutId = rs.getInt("id");
                    System.out.println("Last workout ID found: " + workoutId); // Debug statement
                    return workoutId;
                } else {
                    System.out.println("No workout ID found for username: " + username); // Debug statement
                    return -1;
                }
            }
        }
    }



    public ResultSet getExercisesByMuscleGroup(String muscleGroup) throws SQLException {
        String sql = "SELECT exercise_name, sets FROM Exercises WHERE muscle_group = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, muscleGroup);
            return pstmt.executeQuery();
        }
    }



    public boolean userExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int getMaxWeightForExercise(String username, String exerciseName) throws SQLException {
        String sql = "SELECT max_weight FROM MaxWeights WHERE username = ? AND exercise_name = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, exerciseName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("max_weight");
                }
            }
        }
        return 0;
    }

    public void saveMaxWeight(String username, String exerciseName, int weight) throws SQLException {
        String sql = "INSERT INTO MaxWeights (username, exercise_name, max_weight) VALUES (?, ?, ?) " +
                "ON CONFLICT(username, exercise_name) DO UPDATE SET max_weight = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, exerciseName);
            pstmt.setInt(3, weight);
            pstmt.setInt(4, weight);
            pstmt.executeUpdate();
        }
    }

    public void saveMuscleGroupSets(String username, String muscleGroup, int sets) throws SQLException {
        String sql = "INSERT INTO MuscleGroupSets (username, muscle_group, week_start_date, sets) VALUES (?, ?, date('now', 'weekday 0', '-7 days'), ?) " +
                "ON CONFLICT(username, muscle_group, week_start_date) DO UPDATE SET sets = sets + ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, muscleGroup);
            pstmt.setInt(3, sets);
            pstmt.setInt(4, sets);
            pstmt.executeUpdate();
        }
    }

    public int getMuscleGroupSets(String username, String muscleGroup) throws SQLException {
        String sql = "SELECT sets FROM MuscleGroupSets WHERE username = ? AND muscle_group = ? AND week_start_date = date('now', 'weekday 0', '-7 days')";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, muscleGroup);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("sets");
                }
            }
        }
        return 0;
    }
    public Map<String, Integer> getAllMuscleGroupSets(String username) throws SQLException {
        String sql = "SELECT muscle_group, sets FROM MuscleGroupSets WHERE username = ? AND week_start_date = date('now', 'weekday 0', '-7 days')";
        Map<String, Integer> muscleGroupSets = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    muscleGroupSets.put(rs.getString("muscle_group"), rs.getInt("sets"));
                }
            }
        }
        return muscleGroupSets;
    }
}

