package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.service.MpaService;

@Service
@Qualifier("mpaServiceInMemoryImpl")
public class MpaServiceInMemoryImpl extends MpaServiceAbstractImpl implements MpaService {
    @Autowired
    public MpaServiceInMemoryImpl(@Qualifier("mpaDaoInMemoryImpl") MpaDao mpaDao) {
        super(mpaDao);
    }
}
