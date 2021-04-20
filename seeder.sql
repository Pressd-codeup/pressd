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
VALUES (1, 'Hi my name is testuser1 and I like running.', '2021-01-01 01:00:00', 'testuser1@gmail.com', 0, 0, '$2a$10$ooIUaOiM08pgpjVbVtkK2uuUS.ZAxhC1sgRk.Ly7.f6vF7/C6UYMm', 'testuser1', 1),
       (2, 'Hi my name is testuser2 and I like bodybuilding.', '2021-02-02 02:00:00', 'testuser2@gmail.com', 0, 0, '$2a$10$wRLxNNcMOOVsYhjDoYbHPe5mcRhMNmQjRzqJy3hucryaaRF7qCCd6', 'testuser2', 1),
       (3, 'Hi my name is testuser3 and I like leg day.', '2021-03-03 03:00:00', 'testuser3@gmail.com', 0, 0, '$2a$10$Yemu5GMAMpXulRjo1cpD7eFgYSU6IsFtsaA.B6z3UUt2gcBTIhPsC', 'testuser3', 1),
       (4, 'Hi my name is testuser4 and I am a coach.', '2021-04-04 04:00:00', 'testuser4@gmail.com', 0, 1, '$2a$10$yyXptLKNnz86PQkYukgiLeq4qrW.qAn/QwIn1ftkNw0Lo/8xUYpgy', 'testuser4', 1);
INSERT INTO posts (id, body, date_posted, title, zipcode, type_id, user_id)
VALUES (1, 'Hi I''m looking for a running partner!', '2021-01-01 01:00:00', 'Come run with me!', 78201, 1, 1),
       (2, 'Hi I''m looking for a coach!', '2021-02-02 02:00:00', 'Teach me how to lift!', 78204, 3, 2),
       (3, 'Hi I''m looking for a lifting bro!', '2021-03-03 03:00:00', 'Come lift with me bro!', 78203, 1, 3),
       (4, 'Hi I''m a coach looking for a client!', '2021-04-04 04:00:00', 'Let me train you.', 78202, 2, 4);
INSERT INTO workouts (id, body, date_posted, title, user_id)
VALUES (1, 'Steps to getting big legs: etc. etc.', '2021-02-02 02:00:00', 'Big Legs Guide', 3),
       (2, 'Running is fun. Back when I was a boy...', '2021-01-02 01:00:00', 'Take Your Run Game to the Next Level', 1),
       (3, 'I''m going to tell you a story about a man named Arnold...', '2021-02-03 04:00:00', 'Get Big in Just 2 Weeks!', 2),
       (4, 'We''ve all been there before. You walk into the gym, go up to your favorite bench, and...', '2021-03-01 02:00:00', 'Bench Better with Barry Biceps', 4);
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
VALUES (1, 'Dang those are some big legs', '2021-02-02 02:05:00', 1, 1),
       (2, 'Those legs are too big for me', '2021-02-02 02:07:00', 2, 1),
       (3, 'I''m a coach and not even my legs are that big', '2021-02-02 02:08:00', 4, 1),
       (4, 'Nice routine. I need to start running more.', '2021-02-02 02:05:00', 2, 2),
       (5, 'Running is dumb and so is this workout.', '2021-02-02 02:07:00', 3, 2),
       (6, 'If you don''t have anything nice to say, testuser3, don''t say anything at all. Sheesh.', '2021-02-02 02:08:00', 1, 2),
       (7, 'Running is hard.', '2021-02-02 02:10:00', 4, 2),
       (8, 'Arnold Schwaernczzinkegger is the man. His name is really hard to spell, though.', '2021-02-02 02:08:00', 1, 3),
       (9, 'Barry Biceps seems like a really cool dude.', '2021-04-02 02:00:00', 3, 4);
INSERT INTO user_workout_rating (id, rating_id, user_id, workout_id)
VALUES (1, 1, 3, 2),
       (2, 5, 4, 2),
       (3, 4, 1, 2),
       (4, 4, 2, 2),
       (5, 4, 1, 1),
       (6, 3, 2, 1),
       (7, 4, 3, 1),
       (8, 5, 4, 1),
       (9, 2, 1, 3),
       (10, 3, 2, 3),
       (11, 2, 3, 3),
       (12, 5, 4, 3),
       (13, 2, 1, 4),
       (14, 3, 2, 4),
       (15, 2, 3, 4),
       (16, 1, 4, 4);

INSERT INTO messages (id, body, date_posted, from_id, to_id)
VALUES (1, 'Hey there just testing', '2020-02-02 01:00:00', 1, 2),
       (2, 'Testing ya right back!', '2020-02-02 02:00:00', 2, 1),
       (3, 'This is yet another test', '2020-02-02 01:01:00', 1, 3),
       (4, 'Roger good buddy', '2020-02-02 02:01:00', 3, 1),
       (5, 'Still out here testing messages', '2020-02-02 01:02:00', 1, 4),
       (6, '10-4, dinosaur', '2020-02-02 02:02:00', 4, 1),
       (7, 'Pretty nice to see that messaging logic works', '2020-02-02 02:03:00', 1, 2),
       (8, 'Let''s test the date logic', '2020-02-02 02:04:00', 1, 2),
       (9, 'Date logic works', '2020-02-02 02:06:00', 2, 1),
       (10, 'Testing date logic', '2020-02-02 02:05:00', 1, 2);

INSERT INTO images (id, delete_url, url, user_id)
VALUES (1, '#', 'https://i.ibb.co/vjQD0sW/pressd-default.png', 0);
