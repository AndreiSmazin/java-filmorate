package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(@Qualifier("genreServiceDbImpl") GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> findAllGenres() {
        log.info("Received GET-request /genres");
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre findGenreById(@PathVariable int id) {
        log.info("Received GET-request /genres/{}", id);
        return genreService.getGenreById(id);
    }
}
