package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Qualifier("filmCrudDaoInMemoryImpl")
@Slf4j
public class FilmCrudDaoInMemoryImpl implements FilmDao {
    private int currentId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public int save(Film film) {
        log.debug("+ save: {}", film);

        film.setId(currentId);
        currentId += 1;

        films.put(film.getId(), film);
        return 0;
    }

    @Override
    public void update(Film film) {
        log.debug("+ update: {}", film);

        films.put(film.getId(), film);
    }

    @Override
    public void deleteById(int id) {
        log.debug("+ deleteById: {}", id);

        films.remove(id);
    }

    @Override
    public List<Film> findPopularFilms() {
        return null;
    }
}
