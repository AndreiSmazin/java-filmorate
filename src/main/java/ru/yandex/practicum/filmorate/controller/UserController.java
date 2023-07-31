package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
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
        userService.deleteAllFriends(id);
        userStorage.deleteUser(id);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriendById(@PathVariable long userId, @PathVariable long friendId) {
        log.info("Получен запрос PUT /users/{}/friends/{}", userId, friendId);

        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriendById(@PathVariable long userId, @PathVariable long friendId) {
        log.info("Получен запрос DELETE /users/{}/friends/{}", userId, friendId);

        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> findFriendsById(@PathVariable long userId) {
        return userService.findFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public List<User> findCommonFriendsById(@PathVariable long userId, @PathVariable long otherUserId) {
        return userService.findCommonFriends(userId, otherUserId);
    }
}

