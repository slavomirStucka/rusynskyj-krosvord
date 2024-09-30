CREATE TABLE IF NOT EXISTS words_table (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    word VARCHAR(256),
    level INTEGER
);

INSERT INTO words_table (word, level) VALUES ('oko', 1);
INSERT INTO words_table (word, level) VALUES ('ono', 1);
INSERT INTO words_table (word, level) VALUES ('meno', 2);
INSERT INTO words_table (word, level) VALUES ('nam', 2);
INSERT INTO words_table (word, level) VALUES ('pole', 3);
INSERT INTO words_table (word, level) VALUES ('ale', 3);
INSERT INTO words_table (word, level) VALUES ('apo', 3);