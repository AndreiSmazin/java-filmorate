package ru.yandex.practicum.filmorate.dao.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.entity.FilmGenre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Qualifier("genresDaoInMemoryImpl")
@Slf4j
public class GenresDaoInMemoryImpl implements GenresDao {
    @Getter
    private final Set<FilmGenre> filmGenres = new HashSet<>();

    @Override
    public void save(int filmId, int genreId) {
        log.debug("+ save FilmGenre: {}, {}", filmId, genreId);

        filmGenres.add(new FilmGenre(filmId, genreId));
    }

    @Override
    public void deleteByFilmId(int filmId) {
        log.debug("+ deleteById FilmGenre: {}", filmId);

        List<FilmGenre> deletedFilmGenres = filmGenres.stream()
                .filter(filmGenre -> filmGenre.getFilmId() == filmId)
                .collect(Collectors.toList());

        for (FilmGenre filmGenre : deletedFilmGenres) {
            filmGenres.remove(filmGenre);
        }
    }
}
