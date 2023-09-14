package ru.yandex.practicum.filmorate.dao;

public interface GenresDao {
    void save(int filmId, int genreId);

    void deleteByFilmId(int filmId);
}
