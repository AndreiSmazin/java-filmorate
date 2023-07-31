package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
    public User findUser(long id) {
        return users.get(validateId(id));
    }

    @Override
    public User createNewUser(User user) {
        user.setId(currentId);
        currentId += 1;

        validateName(user);
        validateFriends(user);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: {}. id={}", user.getName(), user.getId());

        return user;
    }

    @Override
    public User updateUser(User user) {
        validateName(user);
        validateFriends(user);
        users.put(validateId(user.getId()), user);
        log.info("Данные пользователя с id={} обновлены.", user.getId());

        return user;
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
        log.info("Все пользователи удалены");
    }

    @Override
    public void deleteUser(long id) {
        users.remove(validateId(id));
        log.info("Пользователь с id={} удален.", id);
    }

    private long validateId(long id) {
        if (users.containsKey(id)) {
            return id;
        } else {
            throw new IdNotFoundException("пользователь с заданным id не найден", id, "пользователь");
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
