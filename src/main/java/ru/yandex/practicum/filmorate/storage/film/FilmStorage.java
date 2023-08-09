package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    public List<Film> findAllFilms();

    public Optional<Film> findFilm(long id);

    public Film createNewFilm(Film film);

    public Film updateFilm(Film film);

    public void deleteAllFilms();

    public void deleteFilm(long id);
}
