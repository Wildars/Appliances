CREATE TABLE role
(
    id                 BIGINT       NOT NULL,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    deleted_date       TIMESTAMP WITHOUT TIME ZONE,
    deleted_by         VARCHAR(255),
    deleted            BOOLEAN      NOT NULL,
    name               VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE role_permissions
(
    role_id        BIGINT   NOT NULL,
    permissions_id SMALLINT NOT NULL
);

ALTER TABLE role
    ADD CONSTRAINT uc_role_name UNIQUE (name);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permissions_id) REFERENCES permission (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_id) REFERENCES role (id);


INSERT INTO public.role (id, created_by, created_date, last_modified_by, last_modified_date, deleted_date, deleted_by, deleted, name) VALUES (1, null, null, null, null, null, null, false, 'ROLE_ADMIN');
INSERT INTO public.role (id, created_by, created_date, last_modified_by, last_modified_date, deleted_date, deleted_by, deleted, name) VALUES (2, null, null, null, null, null, null, false, 'ROLE_SALEMAN');
INSERT INTO public.role (id, created_by, created_date, last_modified_by, last_modified_date, deleted_date, deleted_by, deleted, name) VALUES (3, null, null, null, null, null, null, false, 'ROLE_SUPPLIER');
