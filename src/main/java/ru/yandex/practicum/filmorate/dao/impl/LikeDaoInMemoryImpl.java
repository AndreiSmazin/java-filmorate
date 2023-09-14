package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Like;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Qualifier("likeDaoInMemoryImpl")
@Slf4j
public class LikeDaoInMemoryImpl implements LikeDao {
    private final Set<Like> likes = new HashSet<>();
    private final FilmDaoInMemoryImpl filmDao;

    public LikeDaoInMemoryImpl(@Qualifier("filmDaoInMemoryImpl") FilmDaoInMemoryImpl filmDao) {
        this.filmDao = filmDao;
    }

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

    @Override
    public List<Film> findPopularFilms() {
        Map<Integer, Integer> filmLikesCounters = new HashMap<>();

        for (int filmId : filmDao.getFilms().keySet()) {
            filmLikesCounters.put(filmId, 0);
        }

        for (Like like : likes) {
            int likeCounter = filmLikesCounters.get(like.getFilmId());
            filmLikesCounters.put(like.getFilmId(), likeCounter + 1);
        }

        return filmDao.getFilms().values().stream()
                .map(Film::getId)
                .sorted(Comparator.comparing(filmLikesCounters::get).reversed())
                .map(filmId -> filmDao.getFilms().get(filmId))
                .collect(Collectors.toList());
    }
}
