CREATE TABLE revinfo
(
    rev      integer NOT NULL,
    revtstmp bigint,
    PRIMARY KEY (rev)
);
create sequence revinfo_seq
    increment by 50;

CREATE TABLE customer_aud
(
    address_id  bigint,
    rev         integer     NOT NULL,
    revtype     smallint,
    first_name  VARCHAR(30),
    last_name   VARCHAR(30),
    email       VARCHAR(30),
    last_update timestamp,
    create_date timestamp,
    active      boolean,
    PRIMARY KEY (address_id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE address_aud
(
    id           bigint      NOT NULL,
    rev          integer      NOT NULL,
    revtype      smallint,
    city_id      BIGINT,
    house_number BIGINT,
    postal_code  INT,
    street       VARCHAR(300),
    district     VARCHAR(300),
    phone        VARCHAR(300),
    last_update  timestamp,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE city_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    id          bigint,
    name        VARCHAR(50),
    last_update timestamp,
    country_id  BIGINT,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE country_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    id          bigint,
    name        VARCHAR(50),
    last_update timestamp,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE actor_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    id          bigint,
    first_name  VARCHAR(30),
    last_name   VARCHAR(30),
    biography   TEXT,
    birth_date  timestamp,
    last_update timestamp,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE film_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    id               bigint,
    title            VARCHAR(100),
    description      VARCHAR(5000),
    release_year     timestamp,
    rental_duration  NUMERIC,
    rental_rate      DOUBLE PRECISION,
    length           NUMERIC,
    replacement_cost DOUBLE PRECISION,
    rating           DOUBLE PRECISION,
    last_update      timestamp,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE language_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    id          bigint,
    name        VARCHAR(30),
    last_update timestamp,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE category_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    id          bigint,
    name        VARCHAR(30),
    last_update timestamp,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE film_actor_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    film_id  BIGINT,
    actor_id BIGINT,
    PRIMARY KEY (film_id, actor_id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE film_language_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    film_id     BIGINT,
    language_id BIGINT,
    PRIMARY KEY (film_id, language_id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE film_category_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    film_id     BIGINT,
    category_id BIGINT,
    PRIMARY KEY (film_id, category_id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE IF NOT EXISTS store_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    address_id  BIGINT,
    last_update timestamp,
    FOREIGN KEY (rev) REFERENCES revinfo (rev),
    PRIMARY KEY (address_id, rev)
);

CREATE TABLE IF NOT EXISTS staff_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    address_id  BIGINT,
    store_id  BIGINT,
    first_name VARCHAR(30),
    last_name VARCHAR(30),
    email VARCHAR(30),
    username VARCHAR(50),
    password VARCHAR(50),
    picture_url VARCHAR(5000),
    last_update timestamp,
    active BOOLEAN,
    FOREIGN KEY (rev) REFERENCES revinfo (rev),
    PRIMARY KEY (address_id, rev)
);

CREATE TABLE IF NOT EXISTS inventory_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    id BIGINT,
    store_id  BIGINT,
    film_id  BIGINT,
    last_update timestamp,
    FOREIGN KEY (rev) REFERENCES revinfo (rev),
    PRIMARY KEY (id, rev)
);

CREATE TABLE IF NOT EXISTS rental_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    id bigint,
    inventory_id  BIGINT,
    staff_id  BIGINT,
    customer_id  BIGINT,
    rental_date timestamp,
    return_date timestamp,
    last_update timestamp,
    FOREIGN KEY (rev) REFERENCES revinfo (rev),
    PRIMARY KEY (id, rev)
);

CREATE TABLE IF NOT EXISTS payment_aud
(
    rev         integer     NOT NULL,
    revtype     smallint,
    id bigint,
    amount numeric,
    payment_date timestamp,
    rental_id  BIGINT,
    staff_id  BIGINT,
    customer_id  BIGINT,
    FOREIGN KEY (rev) REFERENCES revinfo (rev),
    PRIMARY KEY (id, rev)
);
