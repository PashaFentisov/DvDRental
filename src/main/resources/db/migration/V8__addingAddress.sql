CREATE TABLE IF NOT EXISTS address
(
    customer_id  BIGINT,
    city_id  BIGINT,
    house_number BIGINT,
    postal_code INT,
    street VARCHAR(300),
    district VARCHAR(300),
    phone VARCHAR(300) NOT NULL,
    last_update DATE,
    FOREIGN KEY (customer_id) references customer (id),
    FOREIGN KEY (city_id) references city (id),
    PRIMARY KEY (customer_id),
    UNIQUE(postal_code, phone)
);