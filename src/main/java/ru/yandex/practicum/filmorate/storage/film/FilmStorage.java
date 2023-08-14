package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAllFilms();

    Optional<Film> getFilm(long id);

    void addFilm(Film film);

    void updateFilm(Film film);

    void deleteAllFilms();

    Film deleteFilm(long id);
}
