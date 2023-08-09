CREATE TABLE IF NOT EXISTS store
(
    address_id  BIGINT,
    last_update DATE,
    FOREIGN KEY (address_id) references address (id),
    PRIMARY KEY (address_id)

);