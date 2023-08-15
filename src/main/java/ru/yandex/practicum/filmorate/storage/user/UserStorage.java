package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getAllUsers();

    Optional<User> getUser(long id);

    void addUser(User user);

    void updateUser(User user);

    void deleteAllUsers();

    void deleteUser(long id);
}
