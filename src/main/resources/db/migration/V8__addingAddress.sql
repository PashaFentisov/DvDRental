CREATE TABLE IF NOT EXISTS address
(
    id SERIAL PRIMARY KEY,
    city_id  BIGINT,
    house_number BIGINT,
    postal_code INT,
    street VARCHAR(300),
    district VARCHAR(300),
    phone VARCHAR(300) NOT NULL,
    last_update timestamp,
    is_deleted boolean,
    FOREIGN KEY (city_id) references city (id),
    UNIQUE(postal_code, phone)
);

ALTER TABLE customer
    ADD CONSTRAINT fk_customer_address
        FOREIGN KEY (address_id)
            REFERENCES address(id);
