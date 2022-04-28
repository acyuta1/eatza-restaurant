--liquibase formatted sql

--changeset achyutha037:001-create-order-and-order-item-table.sql

create table if not exists orderdb.orders (
    id bigint AUTO_INCREMENT primary key,
    customer_id int not null,
    status varchar(100) null,
    restaurant_id int not null,
    create_date_time datetime(6) null,
    update_date_time datetime(6) null
);

create table if not exists orderdb.ordered_items
(
    id bigint auto_increment
    primary key,
    item_id bigint not null,
    name varchar(255) null,
    price double not null,
    quantity int not null,
    order_id bigint not null,
    constraint FKha3q3tsqr7gri0mbp14hc5mkq
    foreign key (order_id) references orderdb.orders (id)
    on delete cascade
);
