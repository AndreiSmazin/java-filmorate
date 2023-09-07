package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Friend;

import java.util.Optional;

public interface FriendDao {
    Optional<Friend> findById(int userId, int friendId);
    void save(int userId, int friendId);
    void update(int userId, int friendId, boolean isApproved);
    void deleteById(int userId, int friendId);
}
