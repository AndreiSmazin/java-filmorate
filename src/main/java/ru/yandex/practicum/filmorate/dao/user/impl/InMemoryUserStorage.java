package ru.yandex.practicum.filmorate.dao.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUser(int id) {
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
    public void deleteUser(int id) {
        log.debug("+ deleteUser: {}", id);

        users.remove(id);
    }
}
