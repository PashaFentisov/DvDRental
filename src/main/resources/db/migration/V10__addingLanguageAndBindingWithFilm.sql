CREATE TABLE IF NOT EXISTS language
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(30) NOT NULL,
    last_update DATE,
    UNIQUE(name)
);


CREATE TABLE IF NOT EXISTS film_language
(
    film_id     BIGINT,
    language_id BIGINT,
    FOREIGN KEY (film_id) references film (id),
    FOREIGN KEY (language_id) references language (id),
    PRIMARY KEY (film_id, language_id)
);