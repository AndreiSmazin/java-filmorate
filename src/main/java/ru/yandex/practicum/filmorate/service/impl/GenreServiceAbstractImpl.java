package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.util.List;

@Slf4j
public abstract class GenreServiceAbstractImpl {
    protected final GenreDao genreDao;

    @Autowired
    public GenreServiceAbstractImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getAllGenres() {
        log.debug("+ getAllGenres");

        return genreDao.findAll();
    }

    public Genre getGenreById(int id) {
        log.debug("+ getGenreById: {}", id);

        return genreDao.findById(id).orElseThrow(() -> new IdNotFoundException("жанр с заданным id не найден",
                "жанр"));
    }
}
