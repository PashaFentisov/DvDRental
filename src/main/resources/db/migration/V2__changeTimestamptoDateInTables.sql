ALTER TABLE customer DROP COLUMN last_update;
ALTER TABLE customer DROP COLUMN createDate;
ALTER TABLE customer ADD COLUMN last_update date;
ALTER TABLE customer ADD COLUMN create_date date;

ALTER TABLE category DROP COLUMN last_update;
ALTER TABLE category ADD COLUMN last_update date;
