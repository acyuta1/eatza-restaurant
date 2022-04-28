--liquibase formatted sql

--changeset achyutha037:002-add-seed-data.sql
INSERT INTO orderdb.orders (id, create_date_time, customer_id, restaurant_id, status, update_date_time) VALUES (1, '2022-04-11 20:01:49.453887', 1, 1, 'UPDATED', '2022-04-11 20:01:49.453887');
INSERT INTO orderdb.ordered_items (id, item_id, name, price, quantity, order_id) VALUES (1, 1, 'dosa', 50, 10, 1);
INSERT INTO orderdb.ordered_items (id, item_id, name, price, quantity, order_id) VALUES (2, 2, 'idli', 40, 3, 1);