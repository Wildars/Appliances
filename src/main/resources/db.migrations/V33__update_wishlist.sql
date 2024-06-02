-- Добавление нового поля is_served
ALTER TABLE wish_list ADD COLUMN is_served BOOLEAN;

-- Удаление старого поля description
ALTER TABLE wish_list DROP COLUMN description;