package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectFriendIdException;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
public abstract class UserServiceAbstractImpl implements UserService {
    private final UserDao userDao;
    private final FriendDao friendDao;

    public UserServiceAbstractImpl(UserDao userDao,
                                   FriendDao friendDao) {
        this.userDao = userDao;
        this.friendDao = friendDao;
    }

    public List<User> findAllUsers() {
        log.debug("+ findAllUsers");

        return userDao.findAll();
    }

    public User findUser(int id) {
        log.debug("+ findUser: {}", id);

        return userDao.findById(id).orElseThrow(() -> new IdNotFoundException("пользователь с заданным id не найден",
                "пользователь"));
    }

    public User createUser(User user) {
        log.debug("+ createUser: {}", user);

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(userDao.save(user));

        return user;
    }

    public User updateUser(User user) {
        log.debug("+ updateUser: {}", user);

        User targetUser = User.builder().id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
        userDao.update(user);

        return targetUser;
    }

    public void deleteUser(int id) {
        log.debug("+ deleteUser: {}", id);

        findUser(id);

        for (User friend : friendDao.findFriendsById(id)) {
            deleteFriend(id, friend.getId());
        }

        userDao.deleteById(id);
    }

    public void addFriend(int userId, int friendId) {
        log.debug("+ addFriend: {}, {}", userId, friendId);

        validateFriendId(userId, friendId);

        if (friendDao.findById(userId, friendId).isEmpty()) {
            friendDao.save(userId, friendId, true);
            friendDao.save(friendId, userId, false);
        } else {
            friendDao.update(userId, friendId, true);
        }
    }

    public void deleteFriend(int userId, int friendId) {
        log.debug("+ deleteFriend: {}, {}", userId, friendId);

        findUser(userId);
        findUser(friendId);
        validateFriendId(userId, friendId);

        friendDao.deleteById(userId, friendId);
        friendDao.deleteById(friendId, userId);
    }

    public List<User> findFriends(int userId) {
        log.debug("+ findFriends: {}", userId);

        return friendDao.findFriendsById(userId);
    }

    public List<User> findCommonFriends(int userId, int otherUserId) {
        log.debug("+ findCommonFriends: {}, {}", userId, otherUserId);

        validateFriendId(userId, otherUserId);

        return friendDao.findCommonFriends(userId, otherUserId);
    }

    private void validateFriendId(int userId, int friendId) {
        if (userId == friendId) {
            throw new IncorrectFriendIdException("id пользователей совпадают");
        }
    }
}
