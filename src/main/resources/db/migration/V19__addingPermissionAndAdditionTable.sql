CREATE TABLE IF NOT EXISTS permission
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(100)  NOT NULL,
    is_deleted BOOLEAN,
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS role_permission
(
    role_id     BIGINT,
    permission_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES role (id),
    FOREIGN KEY (permission_id) REFERENCES permission (id),
    PRIMARY KEY (role_id, permission_id)
);

insert into permission (name, is_deleted)values ('DELETE_ACCESS', false);
insert into permission (name, is_deleted)values ('SAVE_STAFF_ACCESS', false);
insert into role_permission(role_id, permission_id)values (1, 1);
insert into role_permission(role_id, permission_id)values (2, 1);
insert into role_permission(role_id, permission_id)values (1, 2);

