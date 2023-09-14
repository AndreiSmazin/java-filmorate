package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoDbImpl;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmDaoDbImplTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private FilmDaoDbImpl filmCrudDao;

    @Test
    @DisplayName("Возвращает список всех фильмов")
    void shouldReturnAllFilms() throws Exception {

    }
}