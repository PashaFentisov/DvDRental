CREATE TABLE IF NOT EXISTS phone
(
    id               SERIAL PRIMARY KEY,
    address_id       BIGINT,
    number             VARCHAR(100)  NOT NULL,
    is_main BOOLEAN,
    is_deleted BOOLEAN,
    UNIQUE (number),
    foreign key (address_id) references address(id)
);

CREATE OR REPLACE FUNCTION extract_phone_from_address()
    RETURNS VOID AS
$$
BEGIN
    INSERT INTO phone (address_id, number, is_main, is_deleted)
    SELECT id, phone, true, false
    FROM address;
    ALTER TABLE address DROP COLUMN IF EXISTS phone;
    ALTER TABLE address_aud DROP COLUMN IF EXISTS phone;
END;
$$
    LANGUAGE plpgsql;


SELECT extract_phone_from_address();