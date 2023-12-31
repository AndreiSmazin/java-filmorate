package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Like;
import ru.yandex.practicum.filmorate.service.impl.UserServiceInMemoryImpl;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Qualifier("likeDaoInMemoryImpl")
@Slf4j
public class LikeDaoInMemoryImpl implements LikeDao {
    private final Set<Like> likes = new HashSet<>();
    private final FilmDaoInMemoryImpl filmDao;
    private final UserServiceInMemoryImpl userService;

    @Autowired
    public LikeDaoInMemoryImpl(FilmDaoInMemoryImpl filmDao, UserServiceInMemoryImpl userService) {
        this.filmDao = filmDao;
        this.userService = userService;
    }

    @Override
    public void save(Like like) {
        log.debug("+ save: {}", like);

        userService.findUser(like.getUserId());

        likes.add(like);
    }

    @Override
    public void deleteById(int filmId, int userId) {
        log.debug("+ deleteById: {}, {}", filmId, userId);

        likes.remove(new Like(filmId, userId));
    }

    @Override
    public List<Film> findPopularFilms(int limit) {
        log.debug("+ findPopularFilms: {}", limit);

        return filmDao.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public void updateFilmRate(int filmId, int rate) {
        log.debug("+ updateFilmRate: {}, {}", filmId, rate);

        filmDao.getFilms().get(filmId).setRate(rate);
    }
}
