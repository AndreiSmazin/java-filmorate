package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Like;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class FilmServiceAbstractImpl {
    private final FilmDao filmDao;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final GenresDao genresDao;
    private final LikeDao likeDao;
    private final UserService userService;

    @Autowired
    public FilmServiceAbstractImpl(FilmDao filmDao,
                                   MpaDao mpaDao,
                                   GenreDao genreDao,
                                   GenresDao genresDao,
                                   LikeDao likeDao,
                                   UserService userService) {
        this.filmDao = filmDao;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
        this.genresDao = genresDao;
        this.likeDao = likeDao;
        this.userService = userService;
    }

    public List<Film> findAllFilms() {
        List<Film> films = filmDao.findAll();

        for (Film film : films) {
            film.setGenres(genreDao.findByFilmId(film.getId()));
        }

        return films;
    }

    public Film findFilm(int id) {
        Film film = filmDao.findById(id).orElseThrow(() -> new IdNotFoundException("фильм с заданным id не найден",
                "фильм"));

        film.setGenres(genreDao.findByFilmId(id));
        return film;
    }

    public Film createFilm(Film film) {
        log.debug("+ createFilm: {}", film);

        film.setRate(0);
        film.setMpa(getMpaById(film.getMpa().getId()));
        film.setId(filmDao.save(film));

        if (film.getGenres() != null) {
            List<Genre> genres = film.getGenres().stream()
                    .map(genre -> getGenreById(genre.getId()))
                    .distinct()
                    .peek(genre -> genresDao.save(film.getId(), genre.getId()))
                    .collect(Collectors.toList());
            film.setGenres(genres);
        } else {
            film.setGenres(new ArrayList<>());
        }

        return film;
    }

    public Film updateFilm(Film film) {
        log.debug("+ updateFilm: {}", film);

        int id = film.getId();
        Film targetFilm = findFilm(id);

        targetFilm.setName(film.getName());
        targetFilm.setDescription(film.getDescription());
        targetFilm.setReleaseDate(film.getReleaseDate());
        targetFilm.setDuration(film.getDuration());
        targetFilm.setMpa(getMpaById(film.getMpa().getId()));

        if (film.getGenres() != null) {
            genresDao.deleteByFilmId(id);
            List<Genre> genres = film.getGenres().stream()
                    .map(genre -> getGenreById(genre.getId()))
                    .distinct()
                    .peek(genre -> genresDao.save(film.getId(), genre.getId()))
                    .collect(Collectors.toList());
            targetFilm.setGenres(genres);
        } else {
            targetFilm.setGenres(new ArrayList<>());
        }

        filmDao.update(targetFilm);
        return targetFilm;
    }

    public void deleteFilm(int id) {
        log.debug("+ deleteFilm: {}", id);

        findFilm(id);

        genresDao.deleteByFilmId(id);
        filmDao.deleteById(id);
    }

    public void addLike(int filmId, int userId) {
        log.debug("+ addLike: {}, {}", filmId, userId);

        int filmRate = findFilm(filmId).getRate();
        userService.findUser(userId);

        likeDao.save(new Like(filmId, userId));
        likeDao.updateFilmRate(filmId, filmRate + 1);
    }

    public void deleteLike(int filmId, int userId) {
        log.debug("+ deleteLike: {}, {}", filmId, userId);

        int filmRate = findFilm(filmId).getRate();
        userService.findUser(userId);

        likeDao.deleteById(filmId, userId);
        likeDao.updateFilmRate(filmId, filmRate - 1);
    }

    public List<Film> getPopularFilms(int limit) {
        List<Film> films = likeDao.findPopularFilms().stream()
                .limit(limit)
                .collect(Collectors.toList());

        for (Film film : films) {
            film.setGenres(genreDao.findByFilmId(film.getId()));
        }

        return films;
    }

    public List<Genre> getAllGenres() {
        return genreDao.findAll();
    }

    public Genre getGenreById(int id) {
        return genreDao.findById(id).orElseThrow(() -> new IdNotFoundException("жанр с заданным id не найден",
                "жанр"));
    }

    public List<Mpa> getAllMpa() {
        return mpaDao.findAll();
    }

    public Mpa getMpaById(int id) {
        return mpaDao.findById(id).orElseThrow(() -> new IdNotFoundException("MPA с заданным id не найден",
                "MPA"));
    }
}
