package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    User findUser(int id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> findFriends(int userId);

    List<User> findCommonFriends(int userId, int otherUserId);
}
