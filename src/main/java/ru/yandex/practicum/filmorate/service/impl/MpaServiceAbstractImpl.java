package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.util.List;

@Slf4j
public abstract class MpaServiceAbstractImpl {
    private final MpaDao mpaDao;

    @Autowired
    public MpaServiceAbstractImpl(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> getAllMpa() {
        log.debug("+ getAllMpa");

        return mpaDao.findAll();
    }

    public Mpa getMpaById(int id) {
        log.debug("+ getMpaById: {}", id);

        return mpaDao.findById(id).orElseThrow(() -> new IdNotFoundException("MPA с заданным id не найден",
                "MPA"));
    }
}
