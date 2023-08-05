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
        return checkNull(films.get(id));
    }

    @Override
    public Film createNewFilm(Film film) {
        film.setId(currentId);
        currentId += 1;

        validateLikes(film);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}. id={}", film.getName(), film.getId());

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateLikes(film);
        films.put(validateId(film.getId()), film);
        log.info("Данные фильма с id={} обновлены.", film.getId());

        return film;
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
        log.info("Все фильмы удалены");
    }

    @Override
    public void deleteFilm(long id) {
        films.remove(validateId(id));
        log.info("Фильм с id={} удален.", id);
    }

    private long validateId(long id) {
        if (films.containsKey(id)) {
            return id;
        } else {
            throw new IdNotFoundException("фильм с заданным id не найден", id, "фильм");
        }
    }

    private Film checkNull(Film film)  {
        if (film == null) {
            throw new IdNotFoundException("фильм с заданным id не найден", film.getId(), "фильм");
        }

        return film;
    }

    private void validateLikes(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
    }
}
