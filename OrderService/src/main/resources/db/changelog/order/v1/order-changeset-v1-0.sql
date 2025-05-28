--liquibase formatted sql

--changeset Mihail:v1-0-0
CREATE SEQUENCE orders_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE orders (
    id bigint default nextval('orders_seq') primary key,
    customer_name varchar(64),
    product text,
    quantity int,
    status text,
    created_at timestamp
);

