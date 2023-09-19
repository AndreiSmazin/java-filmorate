package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("filmServiceDbImpl") FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Received GET-request /films");
        return filmService.findAllFilms();
    }

    @GetMapping("{id}")
    public Film find(@PathVariable int id) {
        log.info("Received GET-request /films/{}", id);
        return filmService.findFilm(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Received POST-request /films with body: {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Received PUT-request /films with body: {}", film);
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id) {
        log.info("Received DELETE-request /films/{}", id);
        filmService.deleteFilm(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLikeToFilmById(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Received PUT-request /films/{}/like/{}", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLikeOfFilmById(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Received DELETE-request /films/{}/like/{}", filmId, userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") @Min(1) int count) {
        log.info("Received GET-request /films/popular?count={}", count);
        return filmService.getPopularFilms(count);
    }
}
