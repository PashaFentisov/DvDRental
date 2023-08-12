CREATE TABLE IF NOT EXISTS inventory
(
    id SERIAL PRIMARY KEY,
    store_id  BIGINT,
    film_id  BIGINT,
    last_update timestamp,
    FOREIGN KEY (store_id) references store (address_id),
    FOREIGN KEY (film_id) references film (id)
);
