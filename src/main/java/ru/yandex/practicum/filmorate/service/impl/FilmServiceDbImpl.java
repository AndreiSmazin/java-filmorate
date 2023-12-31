package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenresDao;
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
@Qualifier("filmServiceDbImpl")
@Slf4j
public class FilmServiceDbImpl extends FilmServiceAbstractImpl implements FilmService {
    private final GenresDao genresDao;

    @Autowired
    public FilmServiceDbImpl(@Qualifier("filmDaoDbImpl") FilmDao filmDao,
                             @Qualifier("genresDaoDbImpl") GenresDao genresDao,
                             @Qualifier("likeDaoDbImpl") LikeDao likeDao,
                             @Qualifier("userServiceDbImpl") UserService userService,
                             @Qualifier("genreServiceDbImpl") GenreService genreService,
                             @Qualifier("mpaServiceDbImpl") MpaService mpaService) {
        super(filmDao, likeDao, userService, genreService, mpaService);
        this.genresDao = genresDao;
    }

    @Override
    public List<Film> findAllFilms() {
        log.debug("+ findAllFilm");

        List<Film> films = super.filmDao.findAll();

        for (Film film : films) {
            film.setGenres(genresDao.findByFilmId(film.getId()));
        }

        return films;
    }

    @Override
    public Film findFilm(int id) {
        log.debug("+ findFilm: {}", id);

        Film film = super.filmDao.findById(id).orElseThrow(() -> new IdNotFoundException("фильм с заданным id не найден",
                "фильм"));

        film.setGenres(genresDao.findByFilmId(id));
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        log.debug("+ createFilm: {}", film);

        film.setRate(0);
        film.setMpa(super.mpaService.getMpaById(film.getMpa().getId()));
        film.setId(super.filmDao.save(film));

        if (film.getGenres() != null) {
            film.setGenres(checkAndSaveGenres(film));
        } else {
            film.setGenres(new ArrayList<>());
        }

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
            genresDao.deleteByFilmId(targetFilm.getId());
            targetFilm.setGenres(checkAndSaveGenres(film));
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
        genresDao.deleteByFilmId(id);
    }

    @Override
    public List<Film> getPopularFilms(int limit) {
        log.debug("+ getPopularFilms: {}", limit);

        List<Film> films = super.likeDao.findPopularFilms(limit);

        for (Film film : films) {
            film.setGenres(genresDao.findByFilmId(film.getId()));
        }

        return films;
    }

    private List<Genre> checkAndSaveGenres(Film film) {
         return film.getGenres().stream()
                .map(genre -> super.genreService.getGenreById(genre.getId()))
                .distinct()
                .peek(genre -> genresDao.save(film.getId(), genre.getId()))
                .collect(Collectors.toList());
    }
}
