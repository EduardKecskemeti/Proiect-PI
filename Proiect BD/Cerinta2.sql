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