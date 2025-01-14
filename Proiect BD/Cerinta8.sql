-- 1. Vedere pentru obtinerea utilizatorilor si a totalului caloriilor consumate pentru ziua curentă
CREATE VIEW UserDailyCalories AS
SELECT u.username, SUM(c.calories) AS total_calories
FROM users u
         JOIN consumed_calories c ON u.username = c.username
WHERE c.date = date('now')
        GROUP BY u.username;

-- 2. Vedere pentru obtinerea utilizatorilor si a totalului macronutrientilor consumati pentru ziua curentă
CREATE VIEW UserDailyMacros AS
SELECT u.username, SUM(m.proteins) AS total_proteins, SUM(m.fats) AS total_fats, SUM(m.carbohydrates) AS total_carbohydrates
FROM users u
         JOIN meals m ON u.username = m.username
WHERE m.date = date('now')
        GROUP BY u.username;

-- 3. Vedere pentru obtinerea utilizatorilor si a greutatii maxime pentru fiecare exercitiu
CREATE VIEW UserMaxWeights AS
SELECT u.username, mw.exercise_name, MAX(mw.max_weight) AS max_weight
FROM users u
         JOIN MaxWeights mw ON u.username = mw.username
GROUP BY u.username, mw.exercise_name;

-- 4. Vedere pentru obtinerea utilizatorilor si a totalului seturilor pentru fiecare grupa de muschi pentru saptamana curenta
CREATE VIEW UserWeeklyMuscleGroupSets AS
SELECT u.username, mgs.muscle_group, SUM(mgs.sets) AS total_sets
FROM users u
         JOIN MuscleGroupSets mgs ON u.username = mgs.username
WHERE mgs.week_start_date = date('now', 'weekday 0', '-7 days')
        GROUP BY u.username, mgs.muscle_group;

-- 5. Vedere pentru obtinerea utilizatorilor si a totalului caloriilor consumate pentru ziua curentă
CREATE VIEW UserCalorieBalance AS
SELECT u.username, u.weight, u.age, u.height, u.gender, u.activity_level,
       (CASE
            WHEN u.gender = 'Male' THEN (10 * u.weight) + (6.25 * u.height) - (5 * u.age) + 5
            WHEN u.gender = 'Female' THEN (10 * u.weight) + (6.25 * u.height) - (5 * u.age) - 161
            ELSE 0
           END) *
       (CASE u.activity_level
            WHEN 'Sedentary (little to no exercise)' THEN 1.2
            WHEN 'Lightly active (light exercise 1-3 times a week)' THEN 1.375
            WHEN 'Moderately active (moderate exercise/sports 3-5 times a week)' THEN 1.55
            WHEN 'Very active (hard exercise/sports 6-7 days a week)' THEN 1.725
            WHEN 'Extra active (very hard exercise/sports & physical job or 2x training)' THEN 1.9
            ELSE 1.0
           END) AS daily_calorie_intake,
       COALESCE(SUM(c.calories), 0) AS consumed_calories
FROM users u
         LEFT JOIN consumed_calories c ON u.username = c.username AND c.date = date('now')
        GROUP BY u.username;