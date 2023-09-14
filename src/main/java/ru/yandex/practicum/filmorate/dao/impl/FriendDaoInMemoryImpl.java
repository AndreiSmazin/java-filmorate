package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.entity.Friend;
import ru.yandex.practicum.filmorate.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Qualifier("friendDaoInMemoryImpl")
@Slf4j
public class FriendDaoInMemoryImpl implements FriendDao {
    private final Set<Friend> friends = new HashSet<>();
    private final UserDaoInMemoryImpl userDao;

    public FriendDaoInMemoryImpl(@Qualifier("userDaoInMemoryImpl") UserDaoInMemoryImpl userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<Friend> findById(int userId, int friendId) {
        Friend friend = new Friend(userId, friendId, false);

        for (Friend currentFriend : friends) {
            if (currentFriend.equals(friend)) {
                return Optional.of(friend);
            }
        }

        return Optional.empty();
    }

    @Override
    public void save(int userId, int friendId) {
        log.debug("+ save Friend: {}, {}", userId, friendId);

        friends.add(new Friend(userId, friendId, false));
    }

    @Override
    public void update(int userId, int friendId, boolean isApproved) {
        log.debug("+ update Friend: {}, {}", userId, friendId);

        friends.add(new Friend(userId, friendId, isApproved));
    }

    @Override
    public void deleteById(int userId, int friendId) {
        log.debug("+ delete Friend: {}, {}", userId, friendId);

        friends.remove(new Friend(userId, friendId, false));
    }

    @Override
    public List<User> findFriendsById(int id) {
        Map<Integer, User> users = userDao.getUsers();

        return friends.stream()
                .filter(friend -> friend.getUserId() == id)
                .map(friend -> users.get(friend.getFriendId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends(int id, int otherId) {
        return findFriendsById(id).stream()
                .filter(findFriendsById(otherId)::contains)
                .collect(Collectors.toList());
    }
}
