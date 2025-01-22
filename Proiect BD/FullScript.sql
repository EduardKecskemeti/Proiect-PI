--Trecerea in forma BNCF a bazei de date--
-- Tabel pentru utilizatori
CREATE TABLE usersBCNF (
                           id INTEGER PRIMARY KEY AUTOINCREMENT,
                           username TEXT NOT NULL UNIQUE,
                           password TEXT NOT NULL,
                           weight INTEGER,
                           age INTEGER,
                           height INTEGER,
                           gender TEXT,
                           activity_level TEXT
);

-- Tablel pentru antrenamente
CREATE TABLE workoutsBCNF (
                              id INTEGER PRIMARY KEY AUTOINCREMENT,
                              username TEXT NOT NULL,
                              date TEXT NOT NULL,
                              FOREIGN KEY (username) REFERENCES users(username)
);

-- Tabel pentru exercitii
CREATE TABLE exercisesBCNF (
                               id INTEGER PRIMARY KEY AUTOINCREMENT,
                               muscle_group TEXT NOT NULL,
                               exercise_name TEXT NOT NULL,
                               sets INTEGER NOT NULL
);

-- tabel pentru seturi
CREATE TABLE setsBCNF (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          exercise_name TEXT NOT NULL,
                          weight INTEGER NOT NULL,
                          reps INTEGER NOT NULL,
                          username TEXT,
                          FOREIGN KEY (username) REFERENCES users(username)
);

-- tabel pentru mese
CREATE TABLE mealsBCNF (
                           id INTEGER PRIMARY KEY AUTOINCREMENT,
                           username TEXT,
                           meal_name TEXT,
                           calories INTEGER,
                           date TEXT,
                           proteins INTEGER,
                           fats INTEGER,
                           carbohydrates INTEGER,
                           amount INTEGER,
                           FOREIGN KEY (username) REFERENCES users(username)
);

-- Tabel pentru calorii consumate
CREATE TABLE consumed_caloriesBCNF (
                                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                                       username TEXT,
                                       calories INTEGER,
                                       date TEXT,
                                       FOREIGN KEY (username) REFERENCES users(username)
);

-- Tabel pentru greutati maximale
CREATE TABLE max_weightsBCNF (
                                 username TEXT,
                                 exercise_name TEXT,
                                 max_weight INTEGER,
                                 PRIMARY KEY (username, exercise_name),
                                 FOREIGN KEY (username) REFERENCES users(username)
);

-- Tabel pentru seturi
CREATE TABLE muscle_group_setsBCNF (
                                       username TEXT,
                                       muscle_group TEXT,
                                       week_start_date DATE,
                                       sets INTEGER,
                                       PRIMARY KEY (username, muscle_group, week_start_date),
                                       FOREIGN KEY (username) REFERENCES users(username)
);
--comenzi creare
CREATE TABLE users (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       username TEXT NOT NULL UNIQUE,
                       password TEXT NOT NULL,
                       weight INTEGER,
                       age INTEGER,
                       height INTEGER,
                       gender TEXT,
                       activity_level TEXT
);

CREATE TABLE meals (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       username TEXT,
                       meal_name TEXT,
                       calories INTEGER,
                       date TEXT,
                       proteins INTEGER,
                       fats INTEGER,
                       carbohydrates INTEGER,
                       amount INTEGER,
                       FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE sets (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      exercise_name TEXT NOT NULL,
                      weight INTEGER NOT NULL,
                      reps INTEGER NOT NULL,
                      username TEXT,
                      FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE workouts (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          username TEXT NOT NULL,
                          date TEXT NOT NULL,
                          FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE max_weights (
                             username TEXT,
                             exercise_name TEXT,
                             max_weight INTEGER,
                             PRIMARY KEY (username, exercise_name),
                             FOREIGN KEY (username) REFERENCES users(username)
);
--comenzi modificare
ALTER TABLE users ADD COLUMN email TEXT;
ALTER TABLE meals ADD COLUMN meal_time TEXT;
--comenzi stergere
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS meals;
--comenzi redenumire
ALTER TABLE users RENAME TO app_users;
ALTER TABLE meals RENAME TO daily_meals;
--comenzi trunchiere
TRUNCATE TABLE users;
TRUNCATE TABLE meals;
--verficare existenta tabelelor
SELECT name
FROM sqlite_master
WHERE type='table'
  AND name IN ('users', 'meals', 'sets', 'exercises', 'workouts', 'MaxWeights',
               'MuscleGroupSets', 'consumed_calories');
--afisare structura tabel
PRAGMA table_info(users);
--afisare constrangeri tabele
PRAGMA foreign_key_list(mealsBCNF);
--creare vedere
CREATE VIEW user_meals AS
SELECT username, meal_name, calories, date
        FROM meals;
--modificare vedere
CREATE OR REPLACE VIEW user_meals AS
SELECT username, meal_name, calories, date, proteins, fats, carbohydrates
FROM meals;
--stergere vedere
DROP VIEW IF EXISTS user_meals;
--creare secventa
CREATE SEQUENCE user_seq
    START WITH 1
    INCREMENT BY 1;
--stergere secventa
DROP SEQUENCE IF EXISTS user_seq;
--creare index
CREATE INDEX idx_username ON users(username);
--stergere index
DROP INDEX IF EXISTS idx_username;
--verificare existenta vedere
SELECT name FROM sqlite_master WHERE type='view' AND name='user_meals';
--verificare existenta secventa
SELECT name FROM sqlite_master WHERE type='view' AND name='user_meals';
--verificare existenta index
SELECT name FROM sqlite_master WHERE type='index' AND name='idx_username';
--adaugare date
INSERT INTO users (username, password, weight, age, height, gender, activity_level)
VALUES ('12345', '1', 70, 30, 175, 'Male',
        'Moderately active');
--Modificare date
UPDATE users
SET weight = 75, height = 180
WHERE username = '12345';
--Stergere date
UPDATE users
SET weight = 75, height = 180
WHERE username = '12345';
--imbinare date
MERGE INTO users AS target
    USING (SELECT '12345' AS username, 'new_password' AS password) AS source
    ON (target.username = source.username)
    WHEN MATCHED THEN
        UPDATE SET password = source.password
    WHEN NOT MATCHED THEN
        INSERT (username, password) VALUES (source.username, source.password);
--selectare date
SELECT username, weight, age, height, gender, activity_level
FROM users
WHERE username = '12345';


-- 1. Vedere pentru obtinerea utilizatorilor si a totalului
--    caloriilor consumate pentru ziua curentă
CREATE VIEW UserDailyCalories AS
SELECT u.username, SUM(c.calories) AS total_calories
FROM users u
         JOIN consumed_calories c ON u.username = c.username
WHERE c.date = date('now')
        GROUP BY u.username;

-- 2. Vedere pentru obtinerea utilizatorilor si a totalului macronutrientilor consumati pentru ziua curentă
CREATE VIEW UserDailyMacros AS
SELECT u.username, SUM(m.proteins) AS total_proteins, SUM(m.fats) AS total_fats, SUM(m.carbohydrates)
                                   AS total_carbohydrates
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

-- 4. Vedere pentru obtinerea utilizatorilor si a totalului seturilor pentru fiecare
-- grupa de muschi pentru saptamana curenta
CREATE VIEW UserWeeklyMuscleGroupSets AS
SELECT u.username, mgs.muscle_group, SUM(mgs.sets) AS total_sets
FROM users u
         JOIN MuscleGroupSets mgs ON u.username = mgs.username
WHERE mgs.week_start_date = date('now', 'weekday 0', '-7 days')
        GROUP BY u.username, mgs.muscle_group;

-- 5. Vedere pentru obtinerea utilizatorilor si a aportului zilnic de calorii
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