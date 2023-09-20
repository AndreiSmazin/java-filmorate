package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.service.UserService;

@Service
@Qualifier("userServiceDbImpl")
public class UserServiceDbImpl extends UserServiceAbstractImpl implements UserService {
    public UserServiceDbImpl(@Qualifier("userDaoDbImpl") UserDao userDao,
                             @Qualifier("friendDaoDbImpl") FriendDao friendDao) {
        super(userDao, friendDao);
    }
}
