package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void addUser(User user) {
        log.debug("+ addUser: {}", user);

        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        log.debug("+ updateUser: {}", user);

        users.put(user.getId(), user);
    }

    @Override
    public void deleteAllUsers() {
        log.debug("+ deleteAllUsers");

        users.clear();
    }

    @Override
    public void deleteUser(long id) {
        log.debug("+ deleteUser: {}", id);

        users.remove(id);
    }
}
