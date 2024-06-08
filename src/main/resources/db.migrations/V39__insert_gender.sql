ALTER TABLE gender ADD COLUMN name  VARCHAR(255);

INSERT INTO public.gender (id,name) VALUES (1,'мужской');
INSERT INTO public.gender (id,name) VALUES (2,'женский');

