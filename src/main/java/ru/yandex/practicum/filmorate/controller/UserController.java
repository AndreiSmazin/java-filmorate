package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Getter
    private final Map<Integer, User> users = new HashMap<>();
    @Setter
    private int currentID = 1;

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            name = user.getLogin();
        }
        User newUser = new User(currentID, user.getEmail(), user.getLogin(), name, user.getBirthday());
        currentID += 1;
        users.put(newUser.getId(), newUser);
        log.info("Добавлен новый пользователь: {}. id={}", newUser.getName(), newUser.getId());
        return newUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            String name = user.getName();
            if (name == null || name.isBlank()) {
                name = user.getLogin();
            }
            User newUser = new User(user.getId(), user.getEmail(), user.getLogin(), name, user.getBirthday());
            users.put(newUser.getId(), newUser);
            log.info("Данные пользователя с id={} обновлены.", newUser.getId());
            return newUser;
        } else {
            throw new IdNotFoundException("пользователь с заданным id не найден");
        }
    }
}

