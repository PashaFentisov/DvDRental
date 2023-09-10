CREATE TABLE IF NOT EXISTS holiday
(
    id   SERIAL PRIMARY KEY,
    date timestamp,
    UNIQUE (date)
);