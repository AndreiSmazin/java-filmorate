package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    List<Film> findAll();

    Optional<Film> findById(int id);

    int save(Film film);

    void update(Film film);

    void deleteById(int id);

    List<Film> findPopularFilms();
}
