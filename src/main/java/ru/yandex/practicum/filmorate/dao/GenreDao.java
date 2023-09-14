package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreDao {
    List<Genre> findAll();

    Optional<Genre> findById(int id);

    Set<Genre> findByFilmId(int filmId);
}
