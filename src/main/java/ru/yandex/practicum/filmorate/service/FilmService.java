package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.entity.Film;

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
}
