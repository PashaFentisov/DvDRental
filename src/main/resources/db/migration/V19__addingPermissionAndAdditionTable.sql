CREATE TABLE IF NOT EXISTS permission
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(100)  NOT NULL,
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS role_permission
(
    role_id     BIGINT,
    permission_id BIGINT,
    FOREIGN KEY (role_id) references role (id),
    FOREIGN KEY (permission_id) references permission (id),
    PRIMARY KEY (role_id, permission_id)
);