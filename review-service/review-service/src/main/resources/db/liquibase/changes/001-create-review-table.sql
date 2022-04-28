--liquibase formatted sql

--changeset achyutha037:001-create-review-table.sql

create table if not exists reviewdb.review (
                                    id int not null AUTO_INCREMENT,
                                    review_text varchar(255) not null,
                                    order_id int not null,
                                    customer_id int not null,
                                    primary key (id)
);