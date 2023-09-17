INSERT INTO public.films (name, description, release_date, duration, mpa_id, rate)
VALUES ('Американский пирог', 'Четверо друзей-школьников договариваются вместе сделать всё, чтобы лишиться девственности до конца их последнего учебного года в школе.', '2000-01-20', 96, 4, 0);
INSERT INTO public.films (name, description, release_date, duration, mpa_id, rate)
VALUES ('Интерстеллар', 'Группа ученых отправляются в космическое путешествие в поисках планеты с подходящими для человечества условиями.', '2014-10-26', 169, 3, 2);
INSERT INTO public.films (name, description, release_date, duration, mpa_id, rate)
VALUES ('Крепкий орешек', 'Полицейский Джон Макклейн ведет смертельную схватку с бандой политических террористов, взявших в заложники два десятка человек, в числе которых его жена.', '1988-07-12', 133, 4, 0);

INSERT INTO public.genres (film_id, genre_id) VALUES (1, 1);
INSERT INTO public.genres (film_id, genre_id) VALUES (2, 2);
INSERT INTO public.genres (film_id, genre_id) VALUES (3, 6);
INSERT INTO public.genres (film_id, genre_id) VALUES (3, 4);

INSERT INTO public.users (email, login, user_name, birthday)
	VALUES ('galina123@mail.ru','GalGadot','Galya','1990-12-21');
INSERT INTO public.users (email, login, user_name, birthday)
	VALUES ('supermaan@yandex.ru','superman12','Superman','2001-05-12');
INSERT INTO public.users (email, login, user_name, birthday)
	VALUES ('habib@gmail.com','AlHabib','AlHabib','1986-10-19');

INSERT INTO public.likes (film_id, user_id) VALUES (2, 1);
INSERT INTO public.likes (film_id, user_id) VALUES (2, 2);

INSERT INTO public.friend (user_id, friend_id, is_approved) VALUES (1, 2, true);
INSERT INTO public.friend (user_id, friend_id, is_approved) VALUES (2, 1, true);
INSERT INTO public.friend (user_id, friend_id, is_approved) VALUES (1, 3, true);
INSERT INTO public.friend (user_id, friend_id, is_approved) VALUES (3, 1, true)

