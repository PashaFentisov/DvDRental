CREATE TABLE IF NOT EXISTS rental
(
    id SERIAL PRIMARY KEY,
    inventory_id  BIGINT,
    staff_id  BIGINT,
    customer_id  BIGINT,
    rental_date DATE,
    return_date DATE,
    last_update DATE,
    FOREIGN KEY (inventory_id) references inventory (id),
    FOREIGN KEY (staff_id) references staff (address_id),
    FOREIGN KEY (customer_id) references customer (address_id)
);
