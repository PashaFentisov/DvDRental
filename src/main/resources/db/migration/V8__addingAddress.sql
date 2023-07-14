CREATE TABLE IF NOT EXISTS address
(
    customer_id  BIGINT,
    house_number BIGINT,
    postal_code INT,
    street VARCHAR(300),
    district VARCHAR(300),
    phone VARCHAR(300) NOT NULL,
    last_update DATE,
    FOREIGN KEY (customer_id) references customer (id),
    PRIMARY KEY (customer_id)
);