package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

public interface MpaService {
    List<Mpa> getAllMpa();

    Mpa getMpaById(int id);
}
