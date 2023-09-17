package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

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
                             @Qualifier("mpaDaoDbImpl") MpaDao mpaDao,
                             @Qualifier("genreDaoDbImpl") GenreDao genreDao,
                             @Qualifier("genresDaoDbImpl") GenresDao genresDao,
                             @Qualifier("likeDaoDbImpl") LikeDao likeDao,
                             @Qualifier("userServiceDbImpl") UserService userService) {
        super(filmDao, mpaDao, genreDao, likeDao, userService);
        this.genresDao = genresDao;
    }

    @Override
    public List<Film> findAllFilms() {
        List<Film> films = super.filmDao.findAll();

        for (Film film : films) {
            film.setGenres(genresDao.findByFilmId(film.getId()));
        }

        return films;
    }

    @Override
    public Film findFilm(int id) {
        Film film = super.filmDao.findById(id).orElseThrow(() -> new IdNotFoundException("фильм с заданным id не найден",
                "фильм"));

        film.setGenres(genresDao.findByFilmId(id));
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        log.debug("+ createFilm: {}", film);

        film.setRate(0);
        film.setMpa(getMpaById(film.getMpa().getId()));
        film.setId(super.filmDao.save(film));

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

        super.filmDao.update(targetFilm);
        return targetFilm;
    }

    @Override
    public void deleteFilm(int id) {
        log.debug("+ deleteFilm: {}", id);

        findFilm(id);

        genresDao.deleteByFilmId(id);
        super.filmDao.deleteById(id);
    }

    @Override
    public List<Film> getPopularFilms(int limit) {
        List<Film> films = super.likeDao.findPopularFilms().stream()
                .limit(limit)
                .collect(Collectors.toList());

        for (Film film : films) {
            film.setGenres(genresDao.findByFilmId(film.getId()));
        }

        return films;
    }
}
