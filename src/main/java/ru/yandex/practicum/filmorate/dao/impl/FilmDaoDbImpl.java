package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("filmDaoDbImpl")
@Slf4j
public class FilmDaoDbImpl implements FilmDao {
    private static final String FIND_ALL_QUERY = "SELECT f.id, f.name film_name, f.description, f.release_date," +
            " f.duration, m.id mpa_id, m.name mpa_name, f.rate FROM public.films f JOIN public.mpa m ON f.mpa_id = m.id";
    private static final String FIND_BY_ID_QUERY = "SELECT f.id, f.name film_name, f.description, f.release_date," +
            " f.duration, m.id mpa_id, m.name mpa_name, f.rate FROM public.films f JOIN public.mpa m ON f.mpa_id = m.id" +
            " WHERE f.id = :id";
    private static final String SAVE_QUERY = "INSERT INTO public.films (name, description, release_date," +
            " duration, mpa_id, rate) VALUES (:name, :description, :releaseDate, :duration, :mpaId, :rate)";
    private static final String UPDATE_QUERY = "UPDATE public.films SET name = :name," +
            " description = :description, release_date = :releaseDate, duration = :duration," +
            " mpa_id = :mpaId WHERE id = :id";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM public.films WHERE id = :id";

    private static final RowMapper<Film> ROW_MAPPER = (resultSet, i) -> Film.builder()
            .id(resultSet.getInt("id"))
            .name(resultSet.getString("film_name"))
            .description(resultSet.getString("description"))
            .releaseDate(resultSet.getDate("release_date").toLocalDate())
            .duration(resultSet.getInt("duration"))
            .mpa(new Mpa(resultSet.getInt("mpa_id"),
                    resultSet.getString("mpa_name")))
            .rate(resultSet.getInt("rate"))
            .build();

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder = new GeneratedKeyHolder();

    @Autowired
    public FilmDaoDbImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        log.debug("+ findAll Film");

        try {
            return jdbcTemplate.query(FIND_ALL_QUERY, ROW_MAPPER);
        } catch (DataIntegrityViolationException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Film> findById(int id) {
        log.debug("+ findById Film: {}", id);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, parameters, ROW_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int save(Film film) {
        log.debug("+ save Film: {}", film);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        insertParameters(parameters, film);

        jdbcTemplate.update(SAVE_QUERY, parameters, keyHolder, new String[]{"id"});

        return keyHolder.getKey().intValue();
    }

    @Override
    public void update(Film film) {
        log.debug("+ update Film: {}", film);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        insertParameters(parameters, film);

        jdbcTemplate.update(UPDATE_QUERY, parameters, keyHolder, new String[]{"id"});

        if (keyHolder.getKey() == null) {
            throw new IdNotFoundException("фильм с заданным id не найден", "фильм");
        }
    }

    @Override
    public void deleteById(int id) {
        log.debug("+ deleteById Film: {}", id);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);

        jdbcTemplate.update(DELETE_BY_ID_QUERY, parameters);
    }

    private void insertParameters(MapSqlParameterSource parameters, Film film) {
        parameters.addValue("id", film.getId());
        parameters.addValue("name", film.getName());
        parameters.addValue("description", film.getDescription());
        parameters.addValue("releaseDate", film.getReleaseDate());
        parameters.addValue("duration", film.getDuration());
        parameters.addValue("mpaId", film.getMpa().getId());
        parameters.addValue("rate", film.getRate());
    }
}
