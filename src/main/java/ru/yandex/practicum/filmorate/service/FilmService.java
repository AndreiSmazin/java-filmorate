package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(long filmId, long userId) {
        log.debug("Вызван метод 'addLike' с параметрами filmId={}, userId={}", filmId, userId);

        Film targetFilm = validateFilmOnNotNull(filmStorage.findFilm(filmId));
        targetFilm.getLikes().add(validateUserOnNotNull(userStorage.findUser(userId)).getId());

        log.debug("Метод 'addLike' вернул: {}", targetFilm);
        return targetFilm;
    }

    public Film deleteLike(long filmId, long userId) {
        log.debug("Вызван метод 'deleteLike' с параметрами filmId={}, userId={}", filmId, userId);

        Film targetFilm = validateFilmOnNotNull(filmStorage.findFilm(filmId));
        boolean isFound = targetFilm.getLikes().remove(userId);
        if (!isFound) {
            throw new LikeNotFoundException("Лайк пользователя с заданным id не найден в списке лайков фильма " +
                    filmId, filmId, userId);
        }

        log.debug("Метод 'deleteLike' вернул: {}", targetFilm);
        return targetFilm;
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> filmsSortedByRating = filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed()
                        .thenComparing(Film::getId))
                .limit(count)
                .collect(Collectors.toList());

        return filmsSortedByRating;
    }

    private Film validateFilmOnNotNull(Film film) {
        if (film == null) {
            throw new NullObjectException("Полученый из хранилища Film оказался null");
        }

        return film;
    }

    private User validateUserOnNotNull(User user) {
        if (user == null) {
            throw new NullObjectException("Полученый из хранилища User оказался null");
        }

        return user;
    }
}
