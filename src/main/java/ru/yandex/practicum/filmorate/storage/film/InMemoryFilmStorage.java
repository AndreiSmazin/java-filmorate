package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 1;


    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createNewFilm(Film film) {
        film.setId(currentId);
        currentId += 1;
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}. id={}", film.getName(), film.getId());

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Данные фильма с id={} обновлены.", film.getId());

            return film;
        } else {
            throw new IdNotFoundException("фильм с заданным id не найден", film.getId(), "фильм");
        }
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
        log.info("Все фильмы удалены");
    }
}