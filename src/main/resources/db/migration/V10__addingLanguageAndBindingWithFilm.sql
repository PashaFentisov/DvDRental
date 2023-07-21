CREATE TABLE IF NOT EXISTS language
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(30) NOT NULL,
    last_update DATE,
    UNIQUE(name)
);

ALTER TABLE film
    ADD CONSTRAINT fk_language
        FOREIGN KEY (language_id) REFERENCES language (id);