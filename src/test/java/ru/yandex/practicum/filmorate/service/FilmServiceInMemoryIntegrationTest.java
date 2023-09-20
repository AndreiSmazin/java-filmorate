package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.impl.FilmServiceInMemoryImpl;
import ru.yandex.practicum.filmorate.service.impl.UserServiceInMemoryImpl;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmServiceInMemoryIntegrationTest extends FilmServiceIntegrationTest {
    final User testUser1 = User.builder().id(1)
            .email("galina123@mail.ru")
            .login("GalGadot")
            .name("Galya")
            .birthday(LocalDate.parse("1990-12-21"))
            .build();
    final User testUser2 = User.builder().id(2)
            .email("supermaan@yandex.ru")
            .login("superman12")
            .name("Superman")
            .birthday(LocalDate.parse("2001-05-12"))
            .build();
    final User testUser3 = User.builder().id(3)
            .email("habib@gmail.com")
            .login("AlHabib")
            .name("AlHabib")
            .birthday(LocalDate.parse("1986-10-19"))
            .build();

    @Autowired
    public FilmServiceInMemoryIntegrationTest(UserServiceInMemoryImpl userService, FilmServiceInMemoryImpl filmService) {
        super(userService, filmService);
    }

    @BeforeEach
    public void createTestData() {
        super.filmService.createFilm(testFilm1);
        super.filmService.createFilm(testFilm2);
        super.filmService.createFilm(testFilm3);

        super.userService.createUser(testUser1);
        super.userService.createUser(testUser2);
        super.userService.createUser(testUser3);

        super.filmService.addLike(2, 1);
        super.filmService.addLike(2, 2);
    }
}
