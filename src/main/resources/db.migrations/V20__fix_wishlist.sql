ALTER TABLE wish_list DROP COLUMN name;

ALTER TABLE wish_list ADD COLUMN storage_id INT;

ALTER TABLE wish_list ADD CONSTRAINT fk_storage FOREIGN KEY (storage_id) REFERENCES storage(id);