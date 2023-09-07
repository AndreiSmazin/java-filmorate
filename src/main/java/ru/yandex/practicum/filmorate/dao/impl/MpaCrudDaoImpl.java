package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class MpaCrudDaoImpl implements MpaDao {
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM public.mpa ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM public.mpa WHERE id = :id";
    private static final RowMapper<Mpa> ROW_MAPPER = (resultSet, i) -> Mpa.builder()
            .id(resultSet.getInt("id"))
            .name(resultSet.getString("name"))
            .build();

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public MpaCrudDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, ROW_MAPPER);
    }

    @Override
    public Optional<Mpa> findById(int id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, parameters, ROW_MAPPER));
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

