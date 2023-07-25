CREATE TABLE IF NOT EXISTS actor
(
    id          SERIAL PRIMARY KEY,
    first_name  VARCHAR(30) NOT NULL,
    last_name   VARCHAR(30) NOT NULL,
    biography   TEXT        NOT NULL,
    birth_date  DATE,
    last_update DATE,
    UNIQUE (first_name, last_name, birth_date)
);


CREATE TABLE IF NOT EXISTS film_actor
(
    film_id  BIGINT,
    actor_id BIGINT,
    FOREIGN KEY (film_id) references film (id),
    FOREIGN KEY (actor_id) references actor (id),
    PRIMARY KEY (film_id, actor_id)
);