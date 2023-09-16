package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Like;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

@Repository
@Qualifier("likeDaoDbImpl")
@Slf4j
public class LikeDaoDbImpl implements LikeDao {
    private static final String SAVE_QUERY = "INSERT INTO public.likes (film_id, user_id) VALUES (:filmId, :userId)";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM public.likes WHERE (film_id = :filmId) AND" +
            " (user_id = :userId)";
    private static final String FIND_POPULAR_FILMS_QUERY = "SELECT f.id, f.name film_name, f.description," +
            " f.release_date, f.duration, m.id mpa_id, m.name mpa_name, f.rate FROM public.films f" +
            " JOIN public.mpa m ON f.mpa_id = m.id ORDER BY f.rate DESC";

    private static final String UPDATE_RATE_QUERY = "UPDATE public.films SET rate = :rate WHERE id = :id";

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

    @Autowired
    public LikeDaoDbImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
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

    @Override
    public List<Film> findPopularFilms() {
        return jdbcTemplate.query(FIND_POPULAR_FILMS_QUERY, ROW_MAPPER);
    }

    @Override
    public void updateFilmRate(int filmId, int rate) {
        log.debug("+ update FilRate: {}, {}", filmId, rate);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", filmId);
        parameters.addValue("rate", rate);

        jdbcTemplate.update(UPDATE_RATE_QUERY, parameters);
    }
}
