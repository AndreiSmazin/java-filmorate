package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    @Getter
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentID = 1;

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST /films c данными: {}", film);

        film.setId(currentID);
        currentID += 1;
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}. id={}", film.getName(), film.getId());

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT /films c данными: {}", film);

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Данные фильма с id={} обновлены.", film.getId());

            return film;
        } else {
            throw new IdNotFoundException("фильм с заданным id не найден", film.getId(), "фильм");
        }
    }
}
