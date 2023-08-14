package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private long currentId = 1;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film findFilm(long id) {
        return filmStorage.getFilm(id).orElseThrow(() -> new IdNotFoundException("фильм с заданным id не найден",
                "фильм"));
    }

    public Film createFilm(Film film) {
        log.debug("+ createFilm: {}", film);

        film.setId(currentId);
        currentId += 1;

        validateLikes(film);

        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        log.debug("+ updateFilm: {}", film);

        Film targetFilm = findFilm(film.getId());

        validateLikes(film);

        targetFilm.setName(film.getName());
        targetFilm.setDescription(film.getDescription());
        targetFilm.setReleaseDate(film.getReleaseDate());
        targetFilm.setDuration(film.getDuration());

        filmStorage.updateFilm(targetFilm);
        return targetFilm;
    }

    public void deleteAllFilms() {
        log.debug("+ deleteAllFilms");

        filmStorage.deleteAllFilms();
    }

    public void deleteFilm(long id) {
        log.debug("+ deleteFilm: {}", id);

        Film deletedFilm = filmStorage.deleteFilm(id);
        if (deletedFilm == null) {
            throw new IdNotFoundException("фильм с заданным id не найден", "фильм");
        }
    }

    public void addLike(long filmId, long userId) {
        log.debug("+ addLike: {}, {}", filmId, userId);

        Film targetFilm = findFilm(filmId);
        validateUserOnNotNull(userId);

        targetFilm.getLikes().add(userId);

        filmStorage.updateFilm(targetFilm);
    }

    public void deleteLike(long filmId, long userId) {
        log.debug("+ deleteLike: {}, {}", filmId, userId);

        Film targetFilm = findFilm(filmId);

        boolean isFound = targetFilm.getLikes().remove(userId);
        if (!isFound) {
            throw new LikeNotFoundException("Лайк пользователя с заданным id не найден в списке лайков фильма " +
                    filmId, filmId, userId);
        }

        filmStorage.updateFilm(targetFilm);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed()
                        .thenComparing(Film::getId))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateUserOnNotNull(Long id) {
        if (userStorage.getUser(id).isEmpty()) {
            throw new IdNotFoundException("Пользователь с заданным id не найден", "пользователь");
        }
    }

    private void validateLikes(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
    }
}
