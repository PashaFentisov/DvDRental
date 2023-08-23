CREATE TABLE IF NOT EXISTS staff
(
    address_id  BIGINT NOT NULL,
    store_id  BIGINT,
    user_id  BIGINT,
    picture_url VARCHAR(5000),
    last_update timestamp,
    is_deleted boolean,
    FOREIGN KEY (address_id) references address (id),
    FOREIGN KEY (store_id) references store (address_id),
    FOREIGN KEY (user_id) references users (id),
    PRIMARY KEY (address_id)

);