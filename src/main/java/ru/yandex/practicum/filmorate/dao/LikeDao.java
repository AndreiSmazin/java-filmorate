package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Like;

import java.util.List;

public interface LikeDao {
    void save(Like like);

    void deleteById(int filmId, int likeId);

    List<Film> findPopularFilms();

    void updateFilmRate(int filmId, int rate);
}
