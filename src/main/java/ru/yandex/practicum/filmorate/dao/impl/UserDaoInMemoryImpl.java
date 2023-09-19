package ru.yandex.practicum.filmorate.dao.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Qualifier("userDaoInMemoryImpl")
@Slf4j
public class UserDaoInMemoryImpl implements UserDao {
    @Getter
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public int save(User user) {
        log.debug("+ save User: {}", user);

        user.setId(currentId++);
        users.put(user.getId(), user);

        return user.getId();
    }

    @Override
    public void update(User user) {
        log.debug("+ update User: {}", user);

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new IdNotFoundException("пользователь с заданным id не найден", "пользователь");
        }
    }

    @Override
    public void deleteById(int id) {
        log.debug("+ deleteById User: {}", id);

        users.remove(id);
    }
}
