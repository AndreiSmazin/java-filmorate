package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAll();
    Optional<User> findById(int id);
    int save(User user);
    void update(User user);
    void deleteById(int id);
    List<User> findFriendsById(int id);
    List<User> findCommonFriends(int id, int otherId);
}
