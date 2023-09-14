package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;

@Service
@Qualifier("filmServiceInMemoryImpl")
public class FilmServiceInMemoryImpl extends FilmServiceAbstractImpl implements FilmService {
    @Autowired
    public FilmServiceInMemoryImpl(@Qualifier("filmDaoInMemoryImpl") FilmDao filmDao,
                                   @Qualifier("mpaDaoInMemoryImpl") MpaDao mpaDao,
                                   @Qualifier("genreDaoInMemoryImpl") GenreDao genreDao,
                                   @Qualifier("genresDaoInMemoryImpl") GenresDao genresDao,
                                   @Qualifier("likeDaoInMemoryImpl") LikeDao likeDao,
                                   @Qualifier("userServiceInMemoryImpl")UserService userService) {
        super(filmDao, mpaDao, genreDao, genresDao, likeDao, userService);
    }
}
