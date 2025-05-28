--liquibase formatted sql

--changeset Mihail:v1-0-0
CREATE SEQUENCE message_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE message (
    id bigint default nextval('message_seq') primary key,
    message text,
    product_id bigint
);

