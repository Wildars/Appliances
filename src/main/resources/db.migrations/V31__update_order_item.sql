-- -- 1. Удаление существующего внешнего ключа
-- ALTER TABLE order_item DROP CONSTRAINT FK_ORDERITEM_ON_PRODUCT;
--
-- -- 2. Удаление колонки product_id
-- ALTER TABLE order_item DROP COLUMN product_id;

-- 3. Добавление новой колонки filialItem_id
ALTER TABLE order_item ADD COLUMN filial_item_id BIGINT;

-- 4. Добавление нового внешнего ключа для filialItem_id
ALTER TABLE order_item ADD CONSTRAINT FK_ORDERITEM_ON_FILIALITEM FOREIGN KEY (filial_item_id) REFERENCES filial_item (id);
