CREATE TABLE IF NOT EXISTS payment
(
    id SERIAL PRIMARY KEY,
    amount numeric,
    payment_date timestamp,
    rental_id  BIGINT,
    staff_id  BIGINT,
    customer_id  BIGINT,
    is_closed boolean,
    FOREIGN KEY (rental_id) references rental (id),
    FOREIGN KEY (staff_id) references staff (address_id),
    FOREIGN KEY (customer_id) references customer (address_id)
);

CREATE INDEX idx_payment_is_closed ON payment(is_closed, customer_id);
