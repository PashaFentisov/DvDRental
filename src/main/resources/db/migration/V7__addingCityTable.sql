CREATE TABLE IF NOT EXISTS city
(
    id          SERIAL PRIMARY KEY,
    city     VARCHAR(50) NOT NULL,
    last_update DATE,
    country_id   BIGINT,
    FOREIGN KEY (country_id) references country (id)
);