CREATE TABLE IF NOT EXISTS role
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    email VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    role_id  BIGINT,
    is_verified BOOLEAN,
    FOREIGN KEY (role_id) references role (id)
);

