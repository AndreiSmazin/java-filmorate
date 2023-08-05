package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
    public Film findFilm(long id) {
        return validateFilmOnNotNull(films.get(id));
    }

    @Override
    public Film createNewFilm(Film film) {
        log.debug("Вызван метод 'createNewFilm' с параметром film={}", film);

        film.setId(currentId);
        currentId += 1;

        validateLikes(film);
        films.put(film.getId(), film);

        log.debug("Метод 'createNewFilm' вернул: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("Вызван метод 'updateFilm' с параметром film={}", film);

        Film targetFilm = validateFilmOnNotNull(films.get(film.getId()));

        validateLikes(film);

        targetFilm.setName(film.getName());
        targetFilm.setDescription(film.getDescription());
        targetFilm.setReleaseDate(film.getReleaseDate());
        targetFilm.setDuration(film.getDuration());

        log.debug("Метод 'updateFilm' вернул: {}", film);
        return targetFilm;
    }

    @Override
    public void deleteAllFilms() {
        log.debug("Вызван метод 'deleteAllFilms'");

        films.clear();
    }

    @Override
    public void deleteFilm(long id) {
        log.debug("Вызван метод 'deleteFilm' с параметром id={}", id);

        films.remove(validateFilmOnNotNull(films.get(id)).getId());
    }

    private Film validateFilmOnNotNull(Film film)  {
        if (film == null) {
            throw new IdNotFoundException("фильм с заданным id не найден", "фильм");
        }

        return film;
    }

    private void validateLikes(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
    }
}
