package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 1;


    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findFilm(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film createNewFilm(Film film) {
        log.debug("+ createNewFilm: {}", film);

        film.setId(currentId);
        currentId += 1;

        validateLikes(film);
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("+ updateFilm: {}", film);

        Film targetFilm = films.get(film.getId());
        if (targetFilm == null) {
            throw new IdNotFoundException("фильм с заданным id не найден", "фильм");
        }

        validateLikes(film);

        targetFilm.setName(film.getName());
        targetFilm.setDescription(film.getDescription());
        targetFilm.setReleaseDate(film.getReleaseDate());
        targetFilm.setDuration(film.getDuration());

        return targetFilm;
    }

    @Override
    public void deleteAllFilms() {
        log.debug("+ deleteAllFilms");

        films.clear();
    }

    @Override
    public void deleteFilm(long id) {
        log.debug("+ deleteFilm: {}", id);

        Film targetFilm = films.remove(id);
        if (targetFilm == null) {
            throw new IdNotFoundException("фильм с заданным id не найден", "фильм");
        }
    }

    private void validateLikes(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
    }
}
