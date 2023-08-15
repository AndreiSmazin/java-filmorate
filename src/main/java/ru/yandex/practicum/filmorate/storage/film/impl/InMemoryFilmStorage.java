package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getFilm(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void addFilm(Film film) {
        log.debug("+ addFilm: {}", film);

        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
        log.debug("+ updateFilm: {}", film);

        films.put(film.getId(), film);
    }

    @Override
    public void deleteAllFilms() {
        log.debug("+ deleteAllFilms");

        films.clear();
    }

    @Override
    public Film deleteFilm(long id) {
        log.debug("+ deleteFilm: {}", id);

        return films.remove(id);
    }
}
