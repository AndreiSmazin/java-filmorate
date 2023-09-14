package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.entity.Friend;
import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("friendDaoDbImpl")
@Slf4j
public class FriendDaoDbImpl implements FriendDao {
    private static final String FIND_BY_ID_QUERY = "SELECT user_id, friend_id, is_approved FROM public.friend" +
            " WHERE user_id = :userId AND friend_id = :friendId";
    private static final String SAVE_QUERY = "INSERT INTO public.friend (user_id, friend_id, is_approved)" +
            " VALUES (:userId, :friendId , false)";
    private static final String UPDATE_QUERY = "UPDATE public.friend SET is_approved = :isApproved" +
            " WHERE user_id = :userId AND friend_id = :friendId";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM public.friend WHERE user_id = :userId" +
            " AND friend_id = :friendId";
    private static final String FIND_FRIENDS_BY_ID_QUERY = "SELECT id, email, login, user_name, birthday" +
            " FROM public.users WHERE id IN" +
            " (SELECT friend_id FROM public.friend WHERE user_id = :id)";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT id, email, login, user_name, birthday" +
            " FROM public.users WHERE id IN" +
            " (SELECT friend_id FROM public.friend WHERE user_id = :id)" +
            " AND id IN (SELECT friend_id FROM public.friend WHERE user_id = :otherId)";

    private static final RowMapper<Friend> ROW_MAPPER = (resultSet, i) -> Friend.builder()
            .userId(resultSet.getInt("user_id"))
            .friendId(resultSet.getInt("friend_id"))
            .isApproved(resultSet.getBoolean("is_approved"))
            .build();
    private static final RowMapper<User> USER_ROW_MAPPER = (resultSet, i) -> User.builder()
            .id(resultSet.getInt("id"))
            .email(resultSet.getString("email"))
            .login(resultSet.getString("login"))
            .name(resultSet.getString("user_name"))
            .birthday(resultSet.getDate("birthday").toLocalDate())
            .build();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDaoDbImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Friend> findById(int userId, int friendId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        parameters.addValue("friendId", friendId);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, parameters, ROW_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(int userId, int friendId) {
        log.debug("+ save Friend: {}, {}", userId, friendId);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        parameters.addValue("friendId", friendId);

        jdbcTemplate.update(SAVE_QUERY, parameters);
    }

    @Override
    public void update(int userId, int friendId, boolean isApproved) {
        log.debug("+ update Friend: {}, {}, {}", userId, friendId, isApproved);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        parameters.addValue("friendId", friendId);
        parameters.addValue("isApproved", isApproved);

        jdbcTemplate.update(UPDATE_QUERY, parameters);
    }

    @Override
    public void deleteById(int userId, int friendId) {
        log.debug("+ delete Friend: {}, {}", userId, friendId);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        parameters.addValue("friendId", friendId);

        jdbcTemplate.update(DELETE_BY_ID_QUERY, parameters);
    }

    @Override
    public List<User> findFriendsById(int id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);

        return jdbcTemplate.query(FIND_FRIENDS_BY_ID_QUERY, parameters, USER_ROW_MAPPER);
    }

    @Override
    public List<User> findCommonFriends(int id, int otherId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        parameters.addValue("otherId", otherId);

        return jdbcTemplate.query(FIND_COMMON_FRIENDS_QUERY, parameters, USER_ROW_MAPPER);
    }
}
