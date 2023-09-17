package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

public interface GenresDao {
    void save(int filmId, int genreId);

    void deleteByFilmId(int filmId);

    List<Genre> findByFilmId(int filmId);
}
