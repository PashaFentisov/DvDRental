CREATE TABLE IF NOT EXISTS film
(
    id               SERIAL PRIMARY KEY,
    title            VARCHAR(100)  NOT NULL,
    description      VARCHAR(5000) NOT NULL,
    release_year     DATE,
    rental_duration  NUMERIC,
    rental_rate      DOUBLE PRECISION,
    length           NUMERIC,
    replacement_cost DOUBLE PRECISION,
    rating           DOUBLE PRECISION,
    last_update      DATE,
    language_id      BIGINT,
    UNIQUE (title, description, language_id)
);

CREATE TABLE IF NOT EXISTS film_category
(
    film_id     BIGINT,
    category_id BIGINT,
    FOREIGN KEY (film_id) references film (id),
    FOREIGN KEY (category_id) references category (id),
    PRIMARY KEY (film_id, category_id)
);