--liquibase formatted sql

--changeset achyutha037:003-add-token-field-in-customer.sql

alter table customerdb.customer
    add token varchar(100) null;