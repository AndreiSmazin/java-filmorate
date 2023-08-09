package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 1;


    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findUsers(List<Long> ids) {
        return ids.stream()
                .map(id -> {
                    User user = users.get(id);
                    if (user == null) {
                        throw new IdNotFoundException("пользователь с заданным id не найден", "пользователь");
                    }
                    return user;
                })
                .collect(Collectors.toList());
    }

    @Override
    public User createNewUser(User user) {
        log.debug("+ createNewUser: {}", user);

        user.setId(currentId);
        currentId += 1;

        validateName(user);
        validateFriends(user);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User updateUser(User user) {
        log.debug("+ updateUser: {}", user);

        User targetUser = users.get(user.getId());
        if (targetUser == null) {
            throw new IdNotFoundException("пользователь с заданным id не найден", "пользователь");
        }

        validateName(user);
        validateFriends(user);

        targetUser.setEmail(user.getEmail());
        targetUser.setLogin(user.getLogin());
        targetUser.setName(user.getName());
        targetUser.setBirthday(user.getBirthday());

        return targetUser;
    }

    @Override
    public void deleteAllUsers() {
        log.debug("+ deleteAllUsers");

        users.clear();
    }

    @Override
    public void deleteUser(long id) {
        log.debug("+ deleteUser: {}", id);

        User targetUser = users.remove(id);
        if (targetUser == null) {
            throw new IdNotFoundException("пользователь с заданным id не найден", "пользователь");
        }
    }

    private void validateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void validateFriends(User user) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
    }
}
