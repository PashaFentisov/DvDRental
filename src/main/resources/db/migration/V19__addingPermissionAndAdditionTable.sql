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
    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission (id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);
