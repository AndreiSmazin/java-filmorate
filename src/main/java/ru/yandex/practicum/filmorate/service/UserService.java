package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectFriendIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private long currentId = 1;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAllUsers() {
        return userStorage.getAllUsers();
    }

    public User findUser(long id) {
        return userStorage.getUser(id).orElseThrow(() -> new IdNotFoundException("пользователь с заданным id не найден",
                "пользователь"));
    }

    public User createUser(User user) {
        log.debug("+ createUser: {}", user);

        user.setId(currentId);
        currentId += 1;

        validateName(user);
        validateFriends(user);

        userStorage.addUser(user);
        return user;
    }

    public User updateUser(User user) {
        log.debug("+ updateUser: {}", user);

        User targetUser = findUser(user.getId());

        validateName(user);
        validateFriends(user);

        targetUser.setEmail(user.getEmail());
        targetUser.setLogin(user.getLogin());
        targetUser.setName(user.getName());
        targetUser.setBirthday(user.getBirthday());

        userStorage.updateUser(targetUser);
        return targetUser;
    }

    public void deleteAllUsers() {
        log.debug("+ deleteAllUsers");

        userStorage.deleteAllUsers();
    }

    public void deleteUser(long id) {
        log.debug("+ deleteUser: {}", id);

        deleteAllFriends(id);
        userStorage.deleteUser(id);
    }

    public void addFriend(long userId, long friendId) {
        log.debug("+ addFriend: {}, {}", userId, friendId);

        User targetUser = findUser(userId);
        User targetFriend = findUser(friendId);

        validateFriendId(userId, friendId);

        targetUser.getFriends().add(friendId);
        targetFriend.getFriends().add(userId);

        userStorage.updateUser(targetUser);
        userStorage.updateUser(targetFriend);
    }

    public void deleteFriend(long userId, long friendId) {
        log.debug("+ deleteFriend: {}, {}", userId, friendId);

        User targetUser = findUser(userId);
        User targetFriend = findUser(friendId);

        validateFriendId(userId, friendId);

        boolean isFound = targetUser.getFriends().remove(friendId);
        if (!isFound) {
            throw new FriendNotFoundException("пользователь с заданным id не найден в списке друзей пользователя " +
                    userId, userId, friendId);
        }

        targetFriend.getFriends().remove(userId);

        userStorage.updateUser(targetUser);
        userStorage.updateUser(targetFriend);
    }

    public List<User> findFriends(long userId) {
        User targetUser = findUser(userId);

        return targetUser.getFriends().stream()
                .map(this::findUser)
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriends(long userId, long otherUserId) {
        validateFriendId(userId, otherUserId);

        return findFriends(userId).stream()
                .filter(friend -> findFriends(otherUserId).contains(friend))
                .collect(Collectors.toList());
    }

    private void validateFriendId(long userId, long friendId) {
        if (userId == friendId) {
            throw new IncorrectFriendIdException("id пользователей совпадают");
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

    private void deleteAllFriends(long userId) {
        log.debug("+ deleteAllFriends: {}", userId);

        User targetUser = findUser(userId);

        for (User friend : findFriends(userId)) {
            friend.getFriends().remove(userId);
            userStorage.updateUser(friend);
        }

        targetUser.getFriends().clear();
        userStorage.updateUser(targetUser);
    }
}
