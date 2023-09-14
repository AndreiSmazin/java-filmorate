package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Qualifier("mpaDaoInMemoryImpl")
@Slf4j
public class MpaDaoInMemoryImpl implements MpaDao {
    private final Map<Integer, Mpa> mpa = Map.of(1, new Mpa(1, "G"),
            2, new Mpa(2, "PG"),
            3, new Mpa(3, "PG-13"),
            4, new Mpa(4, "R"),
            5, new Mpa(5, "NC-17"));

    @Override
    public List<Mpa> findAll() {
        return mpa.values().stream()
                .sorted(Comparator.comparingInt(Mpa::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Mpa> findById(int id) {
        return Optional.ofNullable(mpa.get(id));
    }
}
