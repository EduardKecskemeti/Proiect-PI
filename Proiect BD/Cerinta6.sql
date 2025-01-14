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
