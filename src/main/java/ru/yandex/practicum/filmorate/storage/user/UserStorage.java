package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    public List<User> findAllUsers();

    public Optional<User> findUser(long id);

    public List<User> findUsers(List<Long> ids);

    public User createNewUser(User user);

    public User updateUser(User user);

    public void deleteAllUsers();

    public void deleteUser(long id);
}
