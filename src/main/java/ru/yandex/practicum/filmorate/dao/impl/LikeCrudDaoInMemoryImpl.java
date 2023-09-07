package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.entity.Like;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Qualifier("likeCrudDaoInMemoryImpl")
@Slf4j
public class LikeCrudDaoInMemoryImpl implements LikeDao {
    private final Set<Like> likes = new HashSet<>();

    @Override
    public void save(Like like) {
        log.debug("+ save: {}", like);

        likes.add(like);
    }

    @Override
    public void deleteById(int filmId, int userId) {
        log.debug("+ deleteById: {}, {}", filmId, userId);

        likes.remove(new Like(filmId, userId));
    }
}
