package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectFriendIdException;
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

    public void addFriend(long userId, long friendId) {
        log.debug("+ addFriend: {}, {}", userId, friendId);

        User targetUser = userStorage.findUser(userId).orElseThrow(() -> new IdNotFoundException("пользователь с" +
                " заданным id не найден", "пользователь"));
        User targetFriend = userStorage.findUser(friendId).orElseThrow(() -> new IdNotFoundException("пользователь с" +
                " заданным id не найден", "пользователь"));

        validateFriendId(userId, friendId);

        targetUser.getFriends().add(friendId);
        targetFriend.getFriends().add(userId);
    }

    public void deleteFriend(long userId, long friendId) {
        log.debug("+ deleteFriend: {}, {}", userId, friendId);

        User targetUser = userStorage.findUser(userId).orElseThrow(() -> new IdNotFoundException("пользователь с" +
                " заданным id не найден", "пользователь"));
        User targetFriend = userStorage.findUser(friendId).orElseThrow(() -> new IdNotFoundException("пользователь с" +
                " заданным id не найден", "пользователь"));

        validateFriendId(userId, friendId);

        boolean isFound = targetUser.getFriends().remove(friendId);
        if (!isFound) {
            throw new FriendNotFoundException("пользователь с заданным id не найден в списке друзей пользователя " +
                    userId, userId, friendId);
        }

        targetFriend.getFriends().remove(userId);
    }

    public void deleteAllFriends(long userId) {
        log.debug("+ deleteAllFriends: {}", userId);

        User targetUser = userStorage.findUser(userId).orElseThrow(() -> new IdNotFoundException("пользователь с" +
                " заданным id не найден", "пользователь"));
        List<User> friends = userStorage.findUsers(new ArrayList<>(targetUser.getFriends()));

        for (User friend : friends) {
            friend.getFriends().remove(userId);
        }

        targetUser.getFriends().clear();
    }

    public List<User> findFriends(long userId) {
        User targetUser = userStorage.findUser(userId).orElseThrow(() -> new IdNotFoundException("пользователь с" +
                " заданным id не найден", "пользователь"));

        return userStorage.findUsers(new ArrayList<>(targetUser.getFriends()));
    }

    public List<User> findCommonFriends(long userId, long otherUserId) {
        validateFriendId(userId, otherUserId);

        User user = userStorage.findUser(userId).orElseThrow(() -> new IdNotFoundException("пользователь с" +
                " заданным id не найден", "пользователь"));
        User otherUser = userStorage.findUser(otherUserId).orElseThrow(() -> new IdNotFoundException("пользователь с" +
                " заданным id не найден", "пользователь"));

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
}
