CREATE TABLE IF NOT EXISTS country(
    id          SERIAL PRIMARY KEY,
    name     VARCHAR(50) NOT NULL,
    last_update DATE
);