package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
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
        filmStorage.findFilm(filmId).getLikes().add(userStorage.findUser(userId).getId());
        log.info("В список лайков фильма {} добавлен лайк пользователя {}", filmId, userId);

        return filmStorage.findFilm(filmId);
    }

    public Film deleteLike(long filmId, long userId) {
        boolean isFound = filmStorage.findFilm(filmId).getLikes().remove(userStorage.findUser(userId).getId());
        if (!isFound) {
            throw new LikeNotFoundException("Лайк пользователя с заданным id не найден в списке лайков фильма " +
                    filmId, filmId, userId);
        }
        log.info("Лайк пользователя {} удален из списка лайков фильма {}", filmId, userId);

        return filmStorage.findFilm(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        validateCount(count);

        List<Film> filmsSortedByRating = filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed()
                        .thenComparing(Film::getId))
                .limit(count)
                .collect(Collectors.toList());

        return filmsSortedByRating;
    }

    private void validateCount(int count) {
        if (count > filmStorage.findAllFilms().size()) {
            count = filmStorage.findAllFilms().size();
        }
    }
}
