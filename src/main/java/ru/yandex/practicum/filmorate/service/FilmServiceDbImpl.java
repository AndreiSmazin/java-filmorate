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
@Qualifier("filmServiceDbImpl")
public class FilmServiceDbImpl extends FilmServiceAbstractImpl implements FilmService {
    @Autowired
    public FilmServiceDbImpl(@Qualifier("filmDaoDbImpl") FilmDao filmDao,
                             @Qualifier("mpaDaoDbImpl") MpaDao mpaDao,
                             @Qualifier("genreDaoDbImpl") GenreDao genreDao,
                             @Qualifier("genresDaoDbImpl") GenresDao genresDao,
                             @Qualifier("likeDaoDbImpl") LikeDao likeDao,
                             @Qualifier("userServiceDbImpl") UserService userService) {
        super(filmDao, mpaDao, genreDao, genresDao, likeDao, userService);
    }
}
