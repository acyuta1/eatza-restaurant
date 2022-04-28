--liquibase formatted sql

--changeset achyutha037:001-create-customer-and-role-tables.sql

create table if not exists customerdb.customer (
                                    id int not null AUTO_INCREMENT,
                                    username varchar(35) not null unique,
                                    email varchar(127) not null unique,
                                    password varchar(255) not null,
                                    first_name varchar(64) not null,
                                    last_name varchar(64) not null,
                                    is_active bit null,
                                    is_non_locked bit null,
                                    primary key (id)
);

create table if not exists customerdb.role (
                                    id int NOT NULL AUTO_INCREMENT,
                                    role_type varchar(255) DEFAULT NULL,
                                    primary key (id)
);

create table if not exists customerdb.user_roles (
                                          customer_id int NOT NULL,
                                          role_id int NOT NULL,
                                          PRIMARY KEY (customer_id,role_id),
                                          foreign key (customer_id) references customer (id),
                                          foreign key (role_id) references role (id)
);
