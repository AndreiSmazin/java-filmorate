package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public List<Film> findAllFilms();

    public Film createNewFilm(Film film);

    public Film updateFilm(Film film);

    public void deleteAllFilms();
}