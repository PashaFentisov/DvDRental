CREATE TABLE IF NOT EXISTS customer(
                                       address_id  BIGINT,
                                       user_id     BIGINT,
                                       last_update date,
                                       createDate  date,
                                       is_deleted  boolean,
                                       balance     numeric,
                                       PRIMARY KEY (address_id)
);

CREATE TABLE IF NOT EXISTS category (
id SERIAL PRIMARY KEY,
name VARCHAR(30) NOT NULL,
last_update date,
is_deleted boolean
);


