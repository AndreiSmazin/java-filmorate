package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("genresDaoDbImpl")
@Slf4j
public class GenresDaoDbImpl implements GenresDao {
    private static final String SAVE_QUERY = "INSERT INTO public.genres (film_id, genre_id) VALUES (:filmId, :genreId)";
    private static final String DELETE_BY_FILM_ID_QUERY = "DELETE FROM public.genres WHERE film_id = :filmId";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT g.id, g.name FROM public.genres gs" +
            " LEFT JOIN public.genre g ON gs.genre_id = g.id WHERE gs.film_id = :filmId ORDER BY g.id";
    private static final RowMapper<Genre> ROW_MAPPER = (resultSet, i) -> Genre.builder()
            .id(resultSet.getInt("id"))
            .name(resultSet.getString("name"))
            .build();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public GenresDaoDbImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int filmId, int genreId) {
        log.debug("+ save Genres: {}, {}", filmId, genreId);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("filmId", filmId);
        parameters.addValue("genreId", genreId);

        jdbcTemplate.update(SAVE_QUERY, parameters);
    }

    @Override
    public void deleteByFilmId(int filmId) {
        log.debug("+ deleteByFilmId Genres: {}", filmId);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("filmId", filmId);

        jdbcTemplate.update(DELETE_BY_FILM_ID_QUERY, parameters);
    }

    @Override
    public List<Genre> findByFilmId(int filmId) {
        log.debug("+ findByFilmId Genres: {}", filmId);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("filmId", filmId);

        return new ArrayList<>(jdbcTemplate.query(FIND_BY_FILM_ID_QUERY, parameters, ROW_MAPPER));
    }
}
