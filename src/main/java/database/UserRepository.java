package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private static final String URL = "jdbc:sqlite:db/userdb.db";

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
            pstmt.setInt(7, amount); // Set the amount in grams
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
}