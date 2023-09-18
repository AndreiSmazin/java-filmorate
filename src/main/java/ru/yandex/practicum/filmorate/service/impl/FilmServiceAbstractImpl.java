package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Like;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@AllArgsConstructor
@Slf4j
public abstract class FilmServiceAbstractImpl {
    protected final FilmDao filmDao;
    protected final LikeDao likeDao;
    private final UserService userService;
    protected final GenreService genreService;
    protected final MpaService mpaService;

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
}
