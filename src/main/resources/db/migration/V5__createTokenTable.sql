CREATE TABLE IF NOT EXISTS token
(
    user_id  BIGINT,
    value VARCHAR(300) NOT NULL,
    create_time timestamp NOT NULL,
    FOREIGN KEY (user_id) references users (id),
    PRIMARY KEY (user_id)
);
