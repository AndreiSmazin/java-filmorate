package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmStorage.findAllFilms();
    }

    @GetMapping("/{id}")
    public Film find(@PathVariable long id) {
        return filmStorage.findFilm(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST /films c данными: {}", film);

        return filmStorage.createNewFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT /films c данными: {}", film);

        return filmStorage.updateFilm(film);
    }

    @DeleteMapping
    public void deleteAll() {
        filmStorage.deleteAllFilms();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        filmStorage.deleteFilm(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLikeToFilmById(@PathVariable long filmId, @PathVariable long userId) {
        log.info("Получен запрос PUT /films/{}/like/{}", filmId, userId);

        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLikeOfFilmById(@PathVariable long filmId, @PathVariable long userId) {
        log.info("Получен запрос DELETE /films/{}/like/{}", filmId, userId);

        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") @Min(1) int count) {
        return filmService.getPopularFilms(count);
    }
}
