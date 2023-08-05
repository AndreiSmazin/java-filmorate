package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectFriendIdException;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(long userId, long friendId) {
        log.debug("Вызван метод 'addFriend' с параметрами userId={}, friendId={}", userId, friendId);

        validateFriendId(userId, friendId);
        User user = validateUserOnNotNull(userStorage.findUser(userId));
        User friend = validateUserOnNotNull(userStorage.findUser(friendId));

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        log.debug("Метод 'addFriend' вернул: {}", user);
        return user;
    }

    public User deleteFriend(long userId, long friendId) {
        log.debug("Вызван метод 'deleteFriend' с параметрами userId={}, friendId={}", userId, friendId);

        validateFriendId(userId, friendId);
        User user = validateUserOnNotNull(userStorage.findUser(userId));
        User friend = validateUserOnNotNull(userStorage.findUser(friendId));

        boolean isFound = user.getFriends().remove(friendId);
        if (!isFound) {
            throw new FriendNotFoundException("пользователь с заданным id не найден в списке друзей пользователя " +
                    userId, userId, friendId);
        }
        friend.getFriends().remove(userId);

        log.debug("Метод 'deleteFriend' вернул: {}", user);
        return user;
    }

    public void deleteAllFriends(long userId) {
        log.debug("Вызван метод 'deleteAllFriends' с параметром userId={}", userId);

        User user = validateUserOnNotNull(userStorage.findUser(userId));
        List<User> friends = userStorage.findUsers(new ArrayList<>(user.getFriends()));

        for (User friend : friends) {
            friend.getFriends().remove(userId);
        }

        user.getFriends().clear();
        log.debug("Метод 'deleteAllFriends' вернул: {}", user);
    }

    public List<User> findFriends(long userId) {
        User user = validateUserOnNotNull(userStorage.findUser(userId));

        return userStorage.findUsers(new ArrayList<>(user.getFriends()));
    }

    public List<User> findCommonFriends(long userId, long otherUserId) {
        validateFriendId(userId, otherUserId);

        User user = validateUserOnNotNull(userStorage.findUser(userId));
        User otherUser = validateUserOnNotNull(userStorage.findUser(otherUserId));

        List<Long> commonFriends = user.getFriends().stream()
                .filter(id -> otherUser.getFriends().contains(id))
                .collect(Collectors.toList());

        return userStorage.findUsers(commonFriends);
    }

    private void validateFriendId(long userId, long friendId) {
        if (userId == friendId) {
            throw new IncorrectFriendIdException("id пользователей совпадают");
        }
    }

    private User validateUserOnNotNull(User user) {
        if (user == null) {
            throw new NullObjectException("Полученый из хранилища User оказался null");
        }

        return user;
    }
}
