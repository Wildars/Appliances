-- Добавление нового поля is_served
ALTER TABLE supply ADD COLUMN supplier_id  BIGINT;

ALTER TABLE supply
    ADD CONSTRAINT FK_SUPPLY_ON_SUPPLIER FOREIGN KEY (supplier_id) REFERENCES supplier (id);