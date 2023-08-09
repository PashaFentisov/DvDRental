CREATE TABLE IF NOT EXISTS customer (
address_id  BIGINT,
first_name VARCHAR(30) NOT NULL,
last_name VARCHAR(30) NOT NULL,
email VARCHAR(30) NOT NULL,
last_update TIMESTAMP,
createDate TIMESTAMP,
active boolean,
PRIMARY KEY (address_id)
);

CREATE TABLE IF NOT EXISTS category (
id SERIAL PRIMARY KEY,
name VARCHAR(30) NOT NULL,
last_update TIMESTAMP
);


