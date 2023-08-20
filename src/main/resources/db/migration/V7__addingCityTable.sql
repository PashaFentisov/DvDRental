CREATE TABLE IF NOT EXISTS city
(
    id          SERIAL PRIMARY KEY,
    name     VARCHAR(50) NOT NULL,
    last_update timestamp,
    country_id   BIGINT,
    is_deleted boolean,
    FOREIGN KEY (country_id) references country (id) ON DELETE CASCADE,
    UNIQUE(name, country_id)
);