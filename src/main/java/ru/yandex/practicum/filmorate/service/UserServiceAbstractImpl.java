package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectFriendIdException;
import ru.yandex.practicum.filmorate.entity.User;

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
        return userDao.findAll();
    }

    public User findUser(int id) {
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

        User targetUser = findUser(user.getId());

        targetUser.setEmail(user.getEmail());
        targetUser.setLogin(user.getLogin());
        targetUser.setName(user.getName());
        targetUser.setBirthday(user.getBirthday());

        userDao.update(user);
        return targetUser;
    }

    public void deleteUser(int id) {
        log.debug("+ deleteUser: {}", id);

        findUser(id);

        userDao.deleteById(id);
    }

    public void addFriend(int userId, int friendId) {
        log.debug("+ addFriend: {}, {}", userId, friendId);

        findUser(userId);
        findUser(friendId);
        validateFriendId(userId, friendId);

        friendDao.save(userId, friendId);

        if (friendDao.findById(friendId, userId).isPresent()) {
            friendDao.update(userId, friendId, true);
            friendDao.update(friendId, userId, true);
        }
    }

    public void deleteFriend(int userId, int friendId) {
        log.debug("+ deleteFriend: {}, {}", userId, friendId);

        findUser(userId);
        findUser(friendId);
        validateFriendId(userId, friendId);

        friendDao.deleteById(userId, friendId);

        if (friendDao.findById(friendId, userId).isPresent()) {
            friendDao.update(friendId, userId, false);
        }
    }

    public List<User> findFriends(int userId) {

        return friendDao.findFriendsById(userId);
    }

    public List<User> findCommonFriends(int userId, int otherUserId) {
        validateFriendId(userId, otherUserId);

        return friendDao.findCommonFriends(userId, otherUserId);
    }

    private void validateFriendId(int userId, int friendId) {
        if (userId == friendId) {
            throw new IncorrectFriendIdException("id пользователей совпадают");
        }
    }
}
