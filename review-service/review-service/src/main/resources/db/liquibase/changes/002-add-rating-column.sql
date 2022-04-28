--liquibase formatted sql

--changeset achyutha037:002-add-rating-column.sql

alter table reviewdb.review add rating varchar(90);