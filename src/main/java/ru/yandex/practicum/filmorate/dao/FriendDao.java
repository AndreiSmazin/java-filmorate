package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Friend;
import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;
import java.util.Optional;

public interface FriendDao {
    Optional<Friend> findById(int userId, int friendId);

    void save(int userId, int friendId, boolean isApproved);

    void update(int userId, int friendId, boolean isApproved);

    void deleteById(int userId, int friendId);

    List<User> findFriendsById(int id);

    List<User> findCommonFriends(int id, int otherId);
}
