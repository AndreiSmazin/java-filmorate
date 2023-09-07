package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getAllUsers();

    Optional<User> getUser(int id);

    void addUser(User user);

    void updateUser(User user);

    void deleteAllUsers();

    void deleteUser(int id);
}
