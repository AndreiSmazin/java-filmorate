package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.util.List;

public class MpaServiceAbstractImpl {
    private final MpaDao mpaDao;

    @Autowired
    public MpaServiceAbstractImpl(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> getAllMpa() {
        return mpaDao.findAll();
    }

    public Mpa getMpaById(int id) {
        return mpaDao.findById(id).orElseThrow(() -> new IdNotFoundException("MPA с заданным id не найден",
                "MPA"));
    }
}
