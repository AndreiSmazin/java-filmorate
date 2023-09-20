package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.service.impl.GenreServiceInMemoryImpl;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreServiceInMemoryImplIntegrationTest extends GenreServiceIntegrationTest {
    @Autowired
    public GenreServiceInMemoryImplIntegrationTest(GenreServiceInMemoryImpl genreService) {
        super(genreService);
    }
}
