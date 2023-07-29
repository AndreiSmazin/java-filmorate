package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;

    @Autowired

    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping
    public List<User> findAll() {
        return userStorage.findAllUsers();
    }

    @GetMapping("/{id}")
    public User find(@PathVariable long id) {
        return userStorage.findUser(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос POST /users c данными: {}", user);

        return userStorage.createNewUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT /users c данными: {}", user);

        return userStorage.updateUser(user);
    }

    @DeleteMapping
    public void deleteAll() {
        userStorage.deleteAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        userStorage.deleteUser(id);
    }
}

