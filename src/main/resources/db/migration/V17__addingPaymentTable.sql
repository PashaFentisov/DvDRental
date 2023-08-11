CREATE TABLE IF NOT EXISTS payment
(
    id SERIAL PRIMARY KEY,
    amount numeric,
    payment_date DATE,
    rental_id  BIGINT,
    staff_id  BIGINT,
    customer_id  BIGINT,
    FOREIGN KEY (rental_id) references rental (id),
    FOREIGN KEY (staff_id) references staff (address_id),
    FOREIGN KEY (customer_id) references customer (address_id)
);
