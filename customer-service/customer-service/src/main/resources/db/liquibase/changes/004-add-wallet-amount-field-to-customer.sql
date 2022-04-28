--liquibase formatted sql

--changeset achyutha037:004-add-wallet-amount-field-to-customer.sql

alter table customerdb.customer
    add wallet_amount bigint null;