--verficare existenta tabelelor
SELECT name
FROM sqlite_master
WHERE type='table'
  AND name IN ('users', 'meals', 'sets', 'exercises', 'workouts', 'MaxWeights', 'MuscleGroupSets', 'consumed_calories');
--afisare structura tabel
PRAGMA table_info(users);
--afisare constrangeri tabele
PRAGMA foreign_key_list(users);