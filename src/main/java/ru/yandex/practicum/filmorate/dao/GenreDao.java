package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    List<Genre> findAll();

    Optional<Genre> findById(int id);
}
