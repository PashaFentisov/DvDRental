CREATE TABLE IF NOT EXISTS staff
(
    address_id  BIGINT NOT NULL,
    store_id  BIGINT,
    first_name VARCHAR(30),
    last_name VARCHAR(30),
    email VARCHAR(30),
    username VARCHAR(50),
    password VARCHAR(50),
    picture_url VARCHAR(5000),
    last_update timestamp,
    active BOOLEAN,
    FOREIGN KEY (address_id) references address (id),
    FOREIGN KEY (store_id) references store (address_id),
    UNIQUE (username),
    PRIMARY KEY (address_id)

);