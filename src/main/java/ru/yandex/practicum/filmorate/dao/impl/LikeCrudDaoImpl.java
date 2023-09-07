package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.entity.Like;

import java.util.List;

@Repository
@Qualifier("likeCrudDaoImpl")
@Slf4j
public class LikeCrudDaoImpl implements LikeDao {
    private NamedParameterJdbcTemplate jdbcTemplate;
    private static final String SAVE_QUERY = "INSERT INTO public.likes (film_id, user_id) VALUES (:filmId, :userId)";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM public.likes WHERE (film_id = :filmId) AND" +
            " (user_id = :userId)";

    @Autowired
    public LikeCrudDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        jdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void save(Like like) {
        log.debug("+ save Like: {}", like);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("filmId", like.getFilmId());
        parameters.addValue("userId", like.getUserId());

        jdbcTemplate.update(SAVE_QUERY, parameters);
    }

    @Override
    public void deleteById(int filmId, int likeId) {
        log.debug("+ deleteById Like: {}, {}", filmId, likeId);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("filmId", filmId);
        parameters.addValue("userId", likeId);

        jdbcTemplate.update(DELETE_BY_ID_QUERY, parameters);
    }
}
