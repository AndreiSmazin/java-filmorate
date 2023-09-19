package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("filmServiceInMemoryImpl")
@Slf4j
public class FilmServiceInMemoryImpl extends FilmServiceAbstractImpl implements FilmService {
    @Autowired
    public FilmServiceInMemoryImpl(@Qualifier("filmDaoInMemoryImpl") FilmDao filmDao,
                                   @Qualifier("likeDaoInMemoryImpl") LikeDao likeDao,
                                   @Qualifier("userServiceInMemoryImpl") UserService userService,
                                   @Qualifier("genreServiceInMemoryImpl") GenreService genreService,
                                   @Qualifier("mpaServiceInMemoryImpl") MpaService mpaService) {
        super(filmDao, likeDao, userService, genreService, mpaService);
    }

    @Override
    public List<Film> findAllFilms() {
        return super.filmDao.findAll();
    }

    @Override
    public Film findFilm(int id) {
        return super.filmDao.findById(id).orElseThrow(() -> new IdNotFoundException("фильм с заданным id не найден",
                "фильм"));
    }

    @Override
    public Film createFilm(Film film) {
        log.debug("+ createFilm: {}", film);

        film.setRate(0);
        film.setMpa(super.mpaService.getMpaById(film.getMpa().getId()));

        if (film.getGenres() != null) {
            film.setGenres(checkGenres(film));
        } else {
            film.setGenres(new ArrayList<>());
        }

        film.setId(super.filmDao.save(film));

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("+ updateFilm: {}", film);

        Film targetFilm = Film.builder().id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(super.mpaService.getMpaById(film.getMpa().getId()))
                .build();
        super.filmDao.update(targetFilm);

        if (film.getGenres() != null) {
            targetFilm.setGenres(checkGenres(film));
        } else {
            targetFilm.setGenres(new ArrayList<>());
        }

        return targetFilm;
    }

    @Override
    public void deleteFilm(int id) {
        log.debug("+ deleteFilm: {}", id);

        findFilm(id);

        super.filmDao.deleteById(id);
    }

    @Override
    public List<Film> getPopularFilms(int limit) {
        return super.likeDao.findPopularFilms(limit);
    }

    private List<Genre> checkGenres(Film film) {
        return film.getGenres().stream()
                .map(genre -> super.genreService.getGenreById(genre.getId()))
                .distinct()
                .collect(Collectors.toList());
    }
}
