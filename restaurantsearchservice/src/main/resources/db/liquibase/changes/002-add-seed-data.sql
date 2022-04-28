--liquibase formatted sql

--changeset achyutha037:002-add-seed-data.sql

insert into restaurantdb.restaurants
values (1, 200, 'breakfast', 'tirupati', 'andhra tiffen', 4.2),
       (2, 250, 'cafe', 'puttur', 'cafe center', 3.8),
       (3, 300, 'chinese', 'chittoor', 'chinese cuisine', 3),
       (4, 400, 'french', 'nagari', 'french restaurant', 4.6),
       (5, 1000, 'italian', 'renigunta', 'italian dishes', 3.9),
       (6, 500, 'veg', 'velloor', 'tiffen center', 4.5),
       (7, 600, 'non veg', 'puttur', 'Aru suvai vunvagam', 5),
       (8, 700, 'non veg', 'nagalapuram', 'Non veg hotel', 2.9),
       (9, 900, 'breakfast', 'Bangalore', 'Indian food', 3.5),
       (10, 400, 'cafe', 'chennai', 'CCD', 4.3),
       (11, 2000, 'italian', 'bhubaneswar', 'Pizza center', 4),
       (12, 900, 'china', 'bangalore', 'Chinese Hotel', 5),
       (13, 1000, 'Veg', 'Chennai', 'Saravana bhavan', 4),
       (14, 500, 'breakfast', 'nagari', 'tiffen center', 3.9),
       (15, 600, 'veg', 'tanjore', 'andhra meals', 4),
       (16, 1000, 'non veg', 'kanchi', 'raghavendhra hotel', 3.9),
       (17, 600, 'veg', 'renigunta', 'venkateswara hotel', 4),
       (18, 1000, 'italian', 'madurai', 'italian dish', 3.6),
       (19, 400, 'parota', 'NH tirupati', 'sai daba', 4.6),
       (20, 1000, 'non veg', 'hyderabad', 'biriyani center', 5);

insert into restaurantdb.menu
values (1, '10', '12', 1)
     , (2, '11', '12', 2)
     , (3, '9', '12', 3)
     , (4, '6', '7', 4)
     , (5, '5', '8', 5)
     , (6, '4', '8', 6)
     , (7, '5', '8', 7)
     , (8, '5', '9', 8)
     , (9, '7', '12', 9)
     , (10, '3', '6', 10)
     , (11, '5', '6', 11)
     , (12, '6', '8', 12)
     , (13, '5', '10', 13)
     , (14, '6', '8', 14)
     , (15, '9', '10', 15)
     , (16, '5', '12', 16)
     , (17, '2', '5', 17)
     , (18, '5', '10', 18)
     , (19, '10', '11', 19)
     , (20, '5', '12', 20);

insert into restaurantdb.menu_items
values (1, 'menu', 'dosa', 50, 1)
     , (2, 'menu', 'idli', 40, 1)
     , (3, 'menu', 'coffee', 60, 2)
     , (4, 'menu', 'cold coffee', 70, 2)
     , (5, 'menu', 'noodles', 100, 3)
     , (6, 'menu', 'chicken', 200, 3)
     , (7, 'menu', 'french fries', 150, 4)
     , (8, 'menu', 'pizza', 250, 5)
     , (9, 'menu', 'burger', 280, 5)
     , (10, 'rice', 'carrot rice', 250, 6)
     , (11, 'mutton', 'mutton fry', 380, 7)
     , (12, 'chicken', 'chicken fry', 300, 8)
     , (13, 'semiya', 'semiya', 150, 9)
     , (14, 'coffee', 'coffee', 200, 10)
     , (15, 'pizza center', 'pizza', 280, 11)
     , (16, 'noodles', 'chicken noodles', 500, 12)
     , (17, 'rice', 'potato rice', 280, 13)
     , (18, 'rava idli', 'rava idli', 280, 14)
     , (19, 'veg', 'veg biriyani', 280, 15)
     , (20, 'fish', 'fish fry', 600, 16)
     , (21, 'veg', 'veg rice', 280, 17)
     , (22, 'italian', 'burger', 280, 18)
     , (23, 'menu', 'parota', 200, 19)
     , (24, 'non veg', 'fish curry', 4890, 20);