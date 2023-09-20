package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("userServiceDbImpl") UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Received GET-request /users");
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User find(@PathVariable int id) {
        log.info("Received GET-request /users/{}", id);
        return userService.findUser(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Received POST-request /users with body: {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Received PUT-request /users with body: {}", user);
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id) {
        log.info("Received DELETE-request /users/{}", id);
        userService.deleteUser(id);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriendById(@PathVariable int userId, @PathVariable int friendId) {
        log.info("Received PUT-request /users/{}/friends/{}", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriendById(@PathVariable int userId, @PathVariable int friendId) {
        log.info("Received DELETE-request /users/{}/friends/{}", userId, friendId);
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> findFriendsById(@PathVariable int userId) {
        log.info("Received GET-request /users/{}/friends", userId);
        return userService.findFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public List<User> findCommonFriendsById(@PathVariable int userId, @PathVariable int otherUserId) {
        log.info("Received GET-request /users/{}/friends/common/{}", userId, otherUserId);
        return userService.findCommonFriends(userId, otherUserId);
    }
}

