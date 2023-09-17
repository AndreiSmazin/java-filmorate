package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("filmServiceInMemoryImpl")
@Slf4j
public class FilmServiceInMemoryImpl extends FilmServiceAbstractImpl implements FilmService {
    @Autowired
    public FilmServiceInMemoryImpl(@Qualifier("filmDaoInMemoryImpl") FilmDao filmDao,
                                   @Qualifier("mpaDaoInMemoryImpl") MpaDao mpaDao,
                                   @Qualifier("genreDaoInMemoryImpl") GenreDao genreDao,
                                   @Qualifier("likeDaoInMemoryImpl") LikeDao likeDao,
                                   @Qualifier("userServiceInMemoryImpl") UserService userService) {
        super(filmDao, mpaDao, genreDao, likeDao, userService);
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
        film.setMpa(getMpaById(film.getMpa().getId()));

        if (film.getGenres() != null) {
            List<Genre> genres = film.getGenres().stream()
                    .map(genre -> getGenreById(genre.getId()))
                    .distinct()
                    .collect(Collectors.toList());
            film.setGenres(genres);
        } else {
            film.setGenres(new ArrayList<>());
        }

        film.setId(super.filmDao.save(film));

        return film;
    }

    @Override
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
            List<Genre> genres = film.getGenres().stream()
                    .map(genre -> getGenreById(genre.getId()))
                    .distinct()
                    .collect(Collectors.toList());
            targetFilm.setGenres(genres);
        } else {
            targetFilm.setGenres(new ArrayList<>());
        }

        super.filmDao.update(targetFilm);
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
        return super.likeDao.findPopularFilms().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
