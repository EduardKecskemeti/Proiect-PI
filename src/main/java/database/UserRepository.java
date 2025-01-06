package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private static final String URL = "jdbc:sqlite:db/userdb.db";
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_MS = 100;
    private Connection connection;



    public UserRepository() throws SQLException {
        this.connection = DriverManager.getConnection(URL);
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeUpdateWithRetry(String sql, String... params) throws SQLException {
        int retries = 0;
        while (true) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setString(i + 1, params[i]);
                }
                return pstmt.executeQuery();
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

    public void saveSet(String exerciseName, int weight, int reps) throws SQLException {
        String sql = "INSERT INTO Sets (exercise_name, weight, reps) VALUES (?, ?, ?)";
        executeUpdateWithRetry(sql, exerciseName, String.valueOf(weight), String.valueOf(reps));
    }

    public void updateBestSet(String exerciseName, int weight, int reps) throws SQLException {
        String sql = "SELECT weight, reps FROM BestSets WHERE exercise_name = ?";
        try (ResultSet rs = executeQueryWithRetry(sql, exerciseName)) {
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

    public void saveMuscleGroup(String muscleGroup, int sets) throws SQLException {
        String sql = "INSERT INTO MuscleGroups (muscle_group, sets) VALUES (?, ?)";
        executeUpdateWithRetry(sql, muscleGroup, String.valueOf(sets));
    }

    public boolean saveWorkoutDate(String date) {
        String sql = "INSERT INTO Workouts (date) VALUES (?)";
        try {
            executeUpdateWithRetry(sql, date);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean addUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public double calculateDailyCalorieIntake(String username) {
        String sql = "SELECT weight, age, height, gender, activity_level FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public boolean logConsumedCalories(String username, int calories) {
        String sql = "INSERT INTO consumed_calories(username, calories, date) VALUES(?, ?, date('now'))";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getConsumedProteins(String username) {
        String sql = "SELECT SUM(proteins) AS total FROM meals WHERE username = ? AND date = date('now')";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getConsumedFats(String username) {
        String sql = "SELECT SUM(fats) AS total FROM meals WHERE username = ? AND date = date('now')";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getConsumedCarbohydrates(String username) {
        String sql = "SELECT SUM(carbohydrates) AS total FROM meals WHERE username = ? AND date = date('now')";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public boolean logMeal(String username, String mealName, int calories, int proteins, int fats, int carbohydrates, int amount) {
        String sql = "INSERT INTO meals(username, meal_name, calories, proteins, fats, carbohydrates, amount, date) VALUES(?, ?, ?, ?, ?, ?, ?, date('now'))";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

    public ResultSet getPresetFoods() {
        String sql = "SELECT * FROM foods";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void resetConsumedMacros(String username) {
        String sql = "DELETE FROM consumed_calories WHERE username = ? AND date = date('now')";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "DELETE FROM meals WHERE username = ? AND date = date('now')";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet getMeals(String username) {
        String sql = "SELECT meal_name, calories, proteins, fats, carbohydrates, date FROM meals WHERE username = ? AND date = date('now')";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public int saveMuscleWorked(String muscleName, int sets, int topSetWeight) {
        String sql = "INSERT INTO MusclesWorked (muscle_name, sets, top_set_weight) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, muscleName);
            pstmt.setInt(2, sets);
            pstmt.setInt(3, topSetWeight);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public boolean saveExerciseDone(int muscleId, String exerciseName) {
        String sql = "INSERT INTO ExercisesDone (muscle_id, exercise_name) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, muscleId);
            pstmt.setString(2, exerciseName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }



    public ResultSet getLastWorkout() throws SQLException {
        String sql = "SELECT date FROM Workouts ORDER BY date DESC LIMIT 1";
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }

    public ResultSet getMuscleGroupsByWorkoutDate(String date) throws SQLException {
        String sql = "SELECT muscle_group, sets FROM MuscleGroups WHERE date = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            return pstmt.executeQuery();
        }
    }

    public ResultSet getExercisesByMuscleGroup(String muscleGroup) throws SQLException {
        String sql = "SELECT exercise_name, sets FROM Exercises WHERE muscle_group = ?";
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, muscleGroup);
        return pstmt.executeQuery();
    }

    public ResultSet getBestSetByExercise(String exerciseName) throws SQLException {
        String sql = "SELECT weight, reps FROM BestSets WHERE exercise_name = ?";
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, exerciseName);
        return pstmt.executeQuery();
    }
}