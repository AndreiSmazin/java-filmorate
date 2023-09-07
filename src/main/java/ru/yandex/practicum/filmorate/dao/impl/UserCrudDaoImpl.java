package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class UserCrudDaoImpl implements UserDao {
    private static final String FIND_ALL_QUERY = "SELECT id, email, login, user_name, birthday FROM public.users";
    private static final String FIND_BY_ID_QUERY = "SELECT id, email, login, user_name, birthday FROM public.users" +
            " WHERE id = :id";
    private static final String SAVE_QUERY = "INSERT INTO public.users (email, login, user_name, birthday)" +
            " VALUES (:email, :login, :userName, :birthday)";
    private static final String UPDATE_QUERY = "UPDATE public.users SET email = :email, login = :login," +
            " user_name = :userName, birthday = :birthday WHERE id = :id";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM public.users WHERE id = :id";
    private static final String FIND_FRIENDS_BY_ID_QUERY = "SELECT id, email, login, user_name, birthday" +
            " FROM public.users WHERE id IN" +
            " (SELECT friend_id FROM public.friend WHERE user_id = :id)";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT id, email, login, user_name, birthday" +
            " FROM public.users WHERE id IN" +
            " (SELECT friend_id FROM public.friend WHERE user_id = :id)" +
            " AND id IN (SELECT friend_id FROM public.friend WHERE user_id = :otherId)";
    private static final RowMapper<User> ROW_MAPPER = (resultSet, i) -> User.builder()
            .id(resultSet.getInt("id"))
            .email(resultSet.getString("email"))
            .login(resultSet.getString("login"))
            .name(resultSet.getString("user_name"))
            .birthday(resultSet.getDate("birthday").toLocalDate())
            .build();

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UserCrudDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, ROW_MAPPER);
    }

    @Override
    public Optional<User> findById(int id) {
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
    public int save(User user) {
        log.debug("+ save User: {}", user);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        insertParameters(parameters, user);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(SAVE_QUERY, parameters, keyHolder, new String[] {"id"});

        return keyHolder.getKey().intValue();
    }

    @Override
    public void update(User user) {
        log.debug("+ update User: {}", user);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        insertParameters(parameters, user);

        jdbcTemplate.update(UPDATE_QUERY, parameters);
    }

    @Override
    public void deleteById(int id) {
        log.debug("+ deleteById User: {}", id);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);

        jdbcTemplate.update(DELETE_BY_ID_QUERY, parameters);
    }

    @Override
    public List<User> findFriendsById(int id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);

        return jdbcTemplate.query(FIND_FRIENDS_BY_ID_QUERY, parameters, ROW_MAPPER);
    }

    @Override
    public List<User> findCommonFriends(int id, int otherId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        parameters.addValue("otherId", otherId);

        return jdbcTemplate.query(FIND_COMMON_FRIENDS_QUERY, parameters, ROW_MAPPER);
    }

    private void insertParameters(MapSqlParameterSource parameters, User user) {
        parameters.addValue("id", user.getId());
        parameters.addValue("email", user.getEmail());
        parameters.addValue("login", user.getLogin());
        parameters.addValue("userName", user.getName());
        parameters.addValue("birthday", user.getBirthday());
    }
}
