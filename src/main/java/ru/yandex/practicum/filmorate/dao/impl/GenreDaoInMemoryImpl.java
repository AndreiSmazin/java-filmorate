package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Qualifier("genreDaoInMemoryImpl")
@Slf4j
public class GenreDaoInMemoryImpl implements GenreDao {
    private final Map<Integer, Genre> genres = Map.of(1, new Genre(1, "Комедия"),
            2, new Genre(2, "Драма"),
            3, new Genre(3, "Мультфильм"),
            4, new Genre(4, "Триллер"),
            5, new Genre(5, "Документальный"),
            6, new Genre(6, "Боевик"));
    private final GenresDaoInMemoryImpl genresDao;

    public GenreDaoInMemoryImpl(@Qualifier("genresDaoInMemoryImpl") GenresDaoInMemoryImpl genresDao) {
        this.genresDao = genresDao;
    }

    @Override
    public List<Genre> findAll() {
        return genres.values().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Genre> findById(int id) {
        return Optional.ofNullable(genres.get(id));
    }

    @Override
    public Set<Genre> findByFilmId(int filmId) {
        return genresDao.getFilmGenres().stream()
                .filter(filmGenre -> filmGenre.getFilmId() == filmId)
                .map(filmGenre -> genres.get(filmGenre.getGenreId()))
                .collect(Collectors.toSet());
    }
}
