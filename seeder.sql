
use pressd_db;
INSERT INTO types (id, name)
VALUES (1, 'partners'),
       (2, 'coaches'),
       (3, 'clients');
INSERT INTO ratings (id, stars)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);
INSERT INTO categories (id, name)
VALUES (1, 'Strength'),
       (2, 'Bodybuilding'),
       (3, 'Endurance'),
       (4, 'Arms'),
       (5, 'Chest'),
       (6, 'Back'),
       (7, 'Legs'),
       (8, 'Core');
INSERT INTO users (id, about, date_joined, email, is_admin, is_coach, password, username, avatar_id)
VALUES (1, 'default', '2021-01-01 00:00:00', 'default', 0, 0, 'default', 'defaultuser', 1),
       (2, 'Hi my name is testuser1 and I like running.', '2021-01-01 01:00:00', 'testuser1@gmail.com', 0, 0, '$2a$10$ooIUaOiM08pgpjVbVtkK2uuUS.ZAxhC1sgRk.Ly7.f6vF7/C6UYMm', 'testuser1', 1),
       (3, 'Hi my name is testuser2 and I like bodybuilding.', '2021-02-02 02:00:00', 'testuser2@gmail.com', 0, 0, '$2a$10$wRLxNNcMOOVsYhjDoYbHPe5mcRhMNmQjRzqJy3hucryaaRF7qCCd6', 'testuser2', 1),
       (4, 'Hi my name is testuser3 and I like leg day.', '2021-03-03 03:00:00', 'testuser3@gmail.com', 0, 0, '$2a$10$Yemu5GMAMpXulRjo1cpD7eFgYSU6IsFtsaA.B6z3UUt2gcBTIhPsC', 'testuser3', 1),
       (5, 'Hi my name is testuser4 and I am a coach.', '2021-04-04 04:00:00', 'testuser4@gmail.com', 0, 1, '$2a$10$yyXptLKNnz86PQkYukgiLeq4qrW.qAn/QwIn1ftkNw0Lo/8xUYpgy', 'testuser4', 1);
INSERT INTO posts (id, body, date_posted, title, type_id, user_id, image_id, city)
VALUES (1, 'Hi I''m looking for a running partner!', '2021-01-01 01:00:00', 'Come run with me!', 1, 2, 1, 'San Antonio'),
       (2, 'Hi I''m looking for a coach!', '2021-02-02 02:00:00', 'Teach me how to lift!', 3, 3, 2, 'Dallas'),
       (3, 'Hi I''m looking for a lifting bro!', '2021-03-03 03:00:00', 'Come lift with me bro!', 1, 4, 3, 'Austin'),
       (4, 'Hi I''m a coach looking for a client!', '2021-04-04 04:00:00', 'Let me train you.', 2, 5, 4, 'Houston');
INSERT INTO workouts (id, body, date_posted, title, user_id, image_id)
VALUES (1, 'Steps to getting big legs: etc. etc.', '2021-02-02 02:00:00', 'Big Legs Guide', 4, 5),
       (2, 'Running is fun. Back when I was a boy...', '2021-01-02 01:00:00', 'Take Your Run Game to the Next Level', 2, 6),
       (3, 'I''m going to tell you a story about a man named Arnold...', '2021-02-03 04:00:00', 'Get Big in Just 2 Weeks!', 3, 7),
       (4, 'We''ve all been there before. You walk into the gym, go up to your favorite bench, and...', '2021-03-01 02:00:00', 'Bench Better with Barry Biceps', 5, 8);
INSERT INTO workout_categories (workout_id, category_id)
VALUES (1, 1),
       (1, 2),
       (1, 7),
       (2, 3),
       (3, 2),
       (3, 4),
       (3, 5),
       (3, 6),
       (3, 7),
       (3, 8),
       (4, 2),
       (4, 4),
       (4, 5);
INSERT INTO comments (id, body, date_posted, user_id, workout_id)
VALUES (1, 'Dang those are some big legs', '2021-02-02 02:05:00', 2, 1),
       (2, 'Those legs are too big for me', '2021-02-02 02:07:00', 3, 1),
       (3, 'I''m a coach and not even my legs are that big', '2021-02-02 02:08:00', 5, 1),
       (4, 'Nice routine. I need to start running more.', '2021-02-02 02:05:00', 3, 2),
       (5, 'Running is dumb and so is this workout.', '2021-02-02 02:07:00', 4, 2),
       (6, 'If you don''t have anything nice to say, testuser3, don''t say anything at all. Sheesh.', '2021-02-02 02:08:00', 2, 2),
       (7, 'Running is hard.', '2021-02-02 02:10:00', 5, 2),
       (8, 'Arnold Schwaernczzinkegger is the man. His name is really hard to spell, though.', '2021-02-02 02:08:00', 2, 3),
       (9, 'Barry Biceps seems like a really cool dude.', '2021-04-02 02:00:00', 4, 4);
INSERT INTO user_workout_rating (id, rating_id, user_id, workout_id)
VALUES (1, 1, 4, 2),
       (2, 5, 5, 2),
       (4, 4, 3, 2),
       (5, 4, 2, 1),
       (6, 1, 3, 1),
       (7, 4, 4, 1),
       (8, 5, 5, 1),
       (9, 2, 2, 3),
       (10, 3, 3, 3),
       (11, 2, 4, 3),
       (12, 5, 5, 3),
       (13, 2, 2, 4),
       (14, 3, 3, 4),
       (15, 2, 4, 4),
       (16, 1, 5, 4);

INSERT INTO messages (id, body, date_posted, from_id, to_id, is_read)
VALUES (1, 'Hey there just testing', '2020-02-02 01:00:00', 2, 3, 0),
       (2, 'Testing ya right back!', '2020-02-02 02:00:00', 3, 2, 0),
       (3, 'This is yet another test', '2020-02-02 01:01:00', 2, 4, 0),
       (4, 'Roger good buddy', '2020-02-02 02:01:00', 4, 2, 0),
       (5, 'Still out here testing messages', '2020-02-02 01:02:00', 2, 5, 0),
       (6, '10-4, dinosaur', '2020-02-02 02:02:00', 5, 2, 0),
       (7, 'Pretty nice to see that messaging logic works', '2020-02-02 02:03:00', 2, 3, 0),
       (8, 'Let''s test the date logic', '2020-02-02 02:04:00', 2, 3, 0),
       (9, 'Date logic works', '2020-02-02 02:06:00', 3, 2, 0),
       (10, 'Testing date logic', '2020-02-02 02:05:00', 2, 3, 0);


INSERT INTO images (id, url, user_id)
VALUES (1, 'https://i.ibb.co/vjQD0sW/pressd-default.png', 1),
       (2, 'https://i.ibb.co/6WsxZ97/bike.jpg', 1),
       (3, 'https://i.ibb.co/Lrm9Xxn/bikes.jpg', 1),
       (4, 'https://i.ibb.co/MBS4Qzw/bruh.jpg',1),
       (5, 'https://i.ibb.co/Z844sj1/do-u-even-lift.jpg', 1),
       (6, 'https://i.ibb.co/Rh8qN29/dumbbell.jpg', 1),
       (7, 'https://i.ibb.co/ZSNJ4JQ/feet.jpg', 1),
       (8, 'https://i.ibb.co/k0BdMmv/fitness.jpg', 1),
       (9, 'https://i.ibb.co/tq709Hd/hiking.jpg', 1),
       (10, 'https://i.ibb.co/51rLhRw/kettlebell.jpg', 1),
       (11, 'https://i.ibb.co/WB6fKzd/nice-sky-bro.jpg', 1),
       (12, 'https://i.ibb.co/JvC7Rhx/runn.jpg', 1),
       (13, 'https://i.ibb.co/HVPwpMY/running.jpg', 1),
       (14, 'https://i.ibb.co/F6XCM2m/sports.jpg', 1),
       (15, 'https://i.ibb.co/bmFmgd0/street.jpg', 1),
       (16, 'https://i.ibb.co/hXKXg0P/yoga-2.jpg', 1),
       (17, 'https://i.ibb.co/WBxNWGb/yogi.jpg', 1);















