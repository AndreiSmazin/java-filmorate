package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Slf4j
public class GenreCrudDaoImpl implements GenreDao {
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM public.genre ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM public.genre WHERE id = :id";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT g.id, g.name FROM public.genres gs" +
            " LEFT JOIN public.genre g ON gs.genre_id = g.id WHERE gs.film_id = :filmId ORDER BY g.id";
    private static final RowMapper<Genre> ROW_MAPPER = (resultSet, i) -> Genre.builder()
            .id(resultSet.getInt("id"))
            .name(resultSet.getString("name"))
            .build();

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public GenreCrudDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, ROW_MAPPER);
    }

    @Override
    public Optional<Genre> findById(int id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, parameters, ROW_MAPPER));
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Set<Genre> findByFilmId(int filmId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("filmId", filmId);

        return new HashSet<>(jdbcTemplate.query(FIND_BY_FILM_ID_QUERY, parameters, ROW_MAPPER));
    }
}
