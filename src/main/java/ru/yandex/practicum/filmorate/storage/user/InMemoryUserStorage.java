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
    public User findUser(long id) {
        return validateUserOnNotNull(users.get(id));
    }

    @Override
    public List<User> findUsers(List<Long> ids) {
        return ids.stream()
                .map(id -> validateUserOnNotNull(users.get(id)))
                .collect(Collectors.toList());
    }

    @Override
    public User createNewUser(User user) {
        log.debug("Вызван метод 'createNewUser' с параметром user={}", user);

        user.setId(currentId);
        currentId += 1;

        validateName(user);
        validateFriends(user);
        users.put(user.getId(), user);

        log.debug("Метод 'createNewUser' вернул: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.debug("Вызван метод 'updateUser' с параметром user={}", user);

        User targetUser = validateUserOnNotNull(users.get(user.getId()));

        validateName(user);
        validateFriends(user);

        targetUser.setEmail(user.getEmail());
        targetUser.setLogin(user.getLogin());
        targetUser.setName(user.getName());
        targetUser.setBirthday(user.getBirthday());

        log.debug("Метод 'updateUser' вернул: {}", targetUser);
        return targetUser;
    }

    @Override
    public void deleteAllUsers() {
        log.debug("Вызван метод 'deleteAllUsers'");

        users.clear();
    }

    @Override
    public void deleteUser(long id) {
        log.debug("Вызван метод 'deleteUser' с параметром id={}", id);

        users.remove(validateUserOnNotNull(users.get(id)).getId());
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

    private User validateUserOnNotNull(User user)  {
        if (user == null) {
            throw new IdNotFoundException("пользователь с заданным id не найден", "пользователь");
        }

        return user;
    }
}
