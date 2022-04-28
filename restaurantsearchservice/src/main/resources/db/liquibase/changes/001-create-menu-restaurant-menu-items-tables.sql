--liquibase formatted sql

--changeset achyutha037:001-create-menu-restaurant-menu-items-tables.sql

create table if not exists restaurantdb.restaurants
(
    id bigint auto_increment
    primary key,
    budget int not null,
    cuisine varchar(255) null,
    location varchar(255) null,
    name varchar(255) null,
    rating double not null
    );

create table if not exists restaurantdb.menu
(
    id bigint auto_increment primary key,
    active_from varchar(255) null,
    active_till varchar(255) null,
    restaurant_id bigint not null,
    constraint FKrr0ffv1553rcdbwsr7vcckipf
    foreign key (restaurant_id) references restaurantdb.restaurants (id)
    on delete cascade
    );



create table if not exists restaurantdb.menu_items
(
    id bigint auto_increment
    primary key,
    description varchar(255) null,
    name varchar(255) null,
    price int not null,
    menu_id bigint not null,
    constraint FKh788p5mv4cmsg53s1ynvibkua
    foreign key (menu_id) references restaurantdb.menu (id)
    on delete cascade
    );

