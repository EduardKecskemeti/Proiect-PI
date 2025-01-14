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