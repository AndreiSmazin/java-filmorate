package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.service.GenreService;

@Service
@Qualifier("genreServiceInMemoryImpl")
public class GenreServiceInMemoryImpl extends GenreServiceAbstractImpl implements GenreService {
    @Autowired
    public GenreServiceInMemoryImpl(@Qualifier("genreDaoInMemoryImpl") GenreDao genreDao) {
        super(genreDao);
    }
}
