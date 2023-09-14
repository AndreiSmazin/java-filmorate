package ru.yandex.practicum.filmorate.dao.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.entity.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Qualifier("filmDaoInMemoryImpl")
@Slf4j
public class FilmDaoInMemoryImpl implements FilmDao {
    @Getter
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

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
        log.debug("+ save Film: {}", film);

        film.setId(currentId++);
        films.put(film.getId(), film);

        return film.getId();
    }

    @Override
    public void update(Film film) {
        log.debug("+ update Film: {}", film);

        films.put(film.getId(), film);
    }

    @Override
    public void deleteById(int id) {
        log.debug("+ deleteById Film: {}", id);

        films.remove(id);
    }
}
