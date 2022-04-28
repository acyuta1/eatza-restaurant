--liquibase formatted sql

--changeset achyutha037:003-add-review-count-column.sql

alter table restaurantdb.restaurants
    add rating_count bigint null;

set @i = 0;

update restaurantdb.restaurants set rating_count = @i:=@i+1 order by rand();