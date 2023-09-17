package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmServiceDbImplIntegrationTest extends FilmServiceIntegrationTest {
    @Autowired
    public FilmServiceDbImplIntegrationTest(UserServiceDbImpl userService, FilmServiceDbImpl filmService) {
        super(userService, filmService);
    }
}
