package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.UserDao;

@Service
@Qualifier("userServiceInMemoryImpl")
public class UserServiceInMemoryImpl extends UserServiceAbstractImpl implements UserService {
    public UserServiceInMemoryImpl(@Qualifier("userDaoInMemoryImpl") UserDao userDao,
                                   @Qualifier("friendDaoInMemoryImpl") FriendDao friendDao) {
        super(userDao, friendDao);
    }
}
