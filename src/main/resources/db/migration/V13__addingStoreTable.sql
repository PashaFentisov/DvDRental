CREATE TABLE IF NOT EXISTS store
(
    address_id  BIGINT,
    last_update timestamp,
    is_deleted boolean,
    FOREIGN KEY (address_id) references address (id),
    PRIMARY KEY (address_id)

);