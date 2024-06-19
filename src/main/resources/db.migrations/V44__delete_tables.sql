drop table if exists public.image cascade;

drop table if exists public.role_permissions cascade;

drop table if exists public.permission cascade;

drop table if exists public.permission_category cascade;

alter table public.product
drop column if exists status_id cascade;

drop table if exists public.status cascade;