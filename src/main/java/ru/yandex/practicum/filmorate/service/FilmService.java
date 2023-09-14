package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

public interface FilmService {
    List<Film> findAllFilms();

    Film findFilm(int id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int id);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getPopularFilms(int limit);

    List<Genre> getAllGenres();

    Genre getGenreById(int id);

    List<Mpa> getAllMpa();

    Mpa getMpaById(int id);
}
