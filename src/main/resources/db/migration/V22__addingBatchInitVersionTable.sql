CREATE TABLE IF NOT EXISTS batch_init_version
(
    id      SERIAL PRIMARY KEY,
    version int default 0
);