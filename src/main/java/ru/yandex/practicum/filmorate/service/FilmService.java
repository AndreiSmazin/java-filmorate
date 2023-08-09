package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
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

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long filmId, long userId) {
        log.debug("+ addLike: {}, {}", filmId, userId);

        Film targetFilm = filmStorage.findFilm(filmId).orElseThrow(() -> new IdNotFoundException("фильм с заданным id" +
                " не найден", "фильм"));
        validateUserOnNotNull(userId);

        targetFilm.getLikes().add(userId);
    }

    public void deleteLike(long filmId, long userId) {
        log.debug("+ deleteLike: {}, {}", filmId, userId);

        Film targetFilm = filmStorage.findFilm(filmId).orElseThrow(() -> new IdNotFoundException("фильм с заданным id" +
                " не найден", "фильм"));

        boolean isFound = targetFilm.getLikes().remove(userId);
        if (!isFound) {
            throw new LikeNotFoundException("Лайк пользователя с заданным id не найден в списке лайков фильма " +
                    filmId, filmId, userId);
        }
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed()
                        .thenComparing(Film::getId))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateUserOnNotNull(Long id) {
        if (userStorage.findUser(id).isEmpty()) {
            throw new IdNotFoundException("Пользователь с заданным id не найден", "пользователь");
        }
    }
}
