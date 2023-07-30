package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectFriendIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(long userId, long friendId) {
        validateFriendId(userId, friendId);
        userStorage.findUser(userId).getFriends().add(userStorage.findUser(friendId).getId());
        log.info("В список друзей пользователя {} добавлен пользователь {}", userId, friendId);

        userStorage.findUser(friendId).getFriends().add(userId);
        log.info("В список друзей пользователя {} добавлен пользователь {}", friendId, userId);

        return userStorage.findUser(userId);
    }

    public User deleteFriend(long userId, long friendId) {
        validateFriendId(userId, friendId);
        boolean isFound = userStorage.findUser(userId).getFriends().remove(userStorage.findUser(friendId).getId());
        if (!isFound) {
            throw new FriendNotFoundException("пользователь с заданным id не найден в списке друзей пользователя " +
                    userId, userId, friendId);
        }

        userStorage.findUser(friendId).getFriends().remove(userId);
        log.info("Пользователь {} удален из списка друзей пользователя {}", friendId, userId);
        log.info("Пользователь {} удален из списка друзей пользователя {}", userId, friendId);

        return userStorage.findUser(userId);
    }

    public void deleteAllFriends(long userId) {
        for (long friendId : userStorage.findUser(userId).getFriends()) {
            userStorage.findUser(friendId).getFriends().remove(userId);
            log.info("Пользователь {} удален из списка друзей пользователя {}", userId, friendId);
        }

        userStorage.findUser(userId).getFriends().clear();
        log.info("Список друзей пользователя {} очищен", userId);
    }

    public List<User> findFriends(long userId) {
        List<User> friends = userStorage.findUser(userId).getFriends().stream()
                .map(id -> userStorage.findUser(id))
                .collect(Collectors.toList());

        return friends;
    }

    public List<User> findCommonFriends(long userId, long otherUserId) {
        List<User> commonFriends = userStorage.findUser(userId).getFriends().stream()
                .filter(id -> userStorage.findUser(otherUserId).getFriends().contains(id))
                .map(id -> userStorage.findUser(id))
                .collect(Collectors.toList());

        return commonFriends;
    }

    private void validateFriendId(long userId, long friendId) {
        if (userId == friendId) {
            throw new IncorrectFriendIdException("id пользователя и id друга совпадают");
        }
    }
}
