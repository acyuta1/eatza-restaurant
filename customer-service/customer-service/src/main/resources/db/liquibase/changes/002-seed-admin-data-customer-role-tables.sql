--liquibase formatted sql

--changeset achyutha037:002-seed-admin-data-customer-role-tables.sql

INSERT INTO customerdb.role(id,role_type) values (1,'ROLE_ADMIN');
INSERT INTO customerdb.role(id,role_type) values (2,'ROLE_USER');

INSERT INTO customer(id, email, username, first_name, last_name, password, is_active, is_non_locked) values (1,'admin@eatza.com','admin','admin','admin','$2a$04$J3f6T7rH5t/DpvMXBFZYTOjW6WqRdR1DdXWXYJ/S1.4d/nWGWxbv.', true, true);
INSERT INTO user_roles(customer_id, role_id) values (1,1);
INSERT INTO user_roles(customer_id, role_id) values (1,2);