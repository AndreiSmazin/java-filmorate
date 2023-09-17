package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Like;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.util.List;

@Slf4j
public abstract class FilmServiceAbstractImpl {
    protected final FilmDao filmDao;
    private final MpaDao mpaDao;
    protected final GenreDao genreDao;
    protected final LikeDao likeDao;
    private final UserService userService;

    @Autowired
    public FilmServiceAbstractImpl(FilmDao filmDao,
                                   MpaDao mpaDao,
                                   GenreDao genreDao,
                                   LikeDao likeDao,
                                   UserService userService) {
        this.filmDao = filmDao;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
        this.likeDao = likeDao;
        this.userService = userService;
    }

    public abstract List<Film> findAllFilms();

    public abstract Film findFilm(int id);

    public abstract Film createFilm(Film film);

    public abstract Film updateFilm(Film film);

    public abstract void deleteFilm(int id);

    public abstract List<Film> getPopularFilms(int limit);


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
