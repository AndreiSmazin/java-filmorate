package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Like;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmDao filmDao;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final GenresDao genresDao;
    private final LikeDao likeDao;
    private final UserService userService;

    @Autowired
    public FilmService(@Qualifier("filmCrudDaoImpl") FilmDao filmDao,
                       MpaDao mpaDao,
                       GenreDao genreDao,
                       GenresDao genresDao,
                       @Qualifier("likeCrudDaoImpl") LikeDao likeDao,
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

        film.setMpa(getMpaById(film.getMpa().getId()));
        film.setId(filmDao.save(film));

        if (film.getGenres() != null) {
            List<Genre> genres = film.getGenres().stream()
                    .map(genre -> getGenreById(genre.getId()))
                    .peek(genre -> genresDao.save(film.getId(), genre.getId()))
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toList());
            film.setGenres(new HashSet<>(genres));
        } else {
            film.setGenres(new HashSet<>());
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
                    .peek(genre -> genresDao.save(film.getId(), genre.getId()))
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toList());
            targetFilm.setGenres(new HashSet<>(genres));
        } else {
            targetFilm.setGenres(new HashSet<>());
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

        findFilm(filmId);
        userService.findUser(userId);

        likeDao.save(new Like(filmId, userId));
    }

    public void deleteLike(int filmId, int userId) {
        log.debug("+ deleteLike: {}, {}", filmId, userId);

        findFilm(filmId);
        userService.findUser(userId);

        likeDao.deleteById(filmId, userId);
    }

    public List<Film> getPopularFilms(int limit) {
        List<Film> films =  filmDao.findPopularFilms().stream()
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
        return mpaDao.findById(id).orElseThrow(() -> new IdNotFoundException("рейтинг с заданным id не найден",
                "рейтинг"));
    }
}
