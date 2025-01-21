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


