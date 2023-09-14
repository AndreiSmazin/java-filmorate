package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.entity.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilmServiceTest {
    private final FilmDao filmDao = new FilmCrudDaoInMemoryImpl();
    private final UserStorage userStorage = mock(UserStorage.class);
    private final FilmServiceDbImpl filmServiceDbImpl = new FilmServiceDbImpl(filmDao, userStorage);

    final Film film1 = new Film(1, "TestFilm1", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L)));
    final Film film2 = new Film(2, "TestFilm2", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 2L, 5L)));
    final Film film3 = new Film(3, "TestFilm3", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>());
    final Film film4 = new Film(4, "TestFilm4", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 2L, 3L, 5L)));
    final Film film5 = new Film(5, "TestFilm5", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 3L, 4L)));
    final Film film6 = new Film(6, "TestFilm6", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 2L, 3L, 4L, 5L)));
    final Film film7 = new Film(7, "TestFilm7", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 2L, 3L)));
    final Film film8 = new Film(8, "TestFilm8", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 4L)));
    final Film film9 = new Film(9, "TestFilm9", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 3L, 5L)));
    final Film film10 = new Film(10, "TestFilm10", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 2L, 3L, 4L)));

    private void createTestFilms() {
        filmServiceDbImpl.createFilm(film1);
        filmServiceDbImpl.createFilm(film2);
        filmServiceDbImpl.createFilm(film3);
        filmServiceDbImpl.createFilm(film4);
        filmServiceDbImpl.createFilm(film5);
        filmServiceDbImpl.createFilm(film6);
        filmServiceDbImpl.createFilm(film7);
        filmServiceDbImpl.createFilm(film8);
        filmServiceDbImpl.createFilm(film9);
        filmServiceDbImpl.createFilm(film10);
    }

    @Test
    @DisplayName("Возвращает фильм по заданному id, выбрасывает исключение фильм с заданным id отсутствует")
    void shouldReturnFilmById() throws Exception {
        final long testId = 2;
        final long wrongTestId = 999;
        createTestFilms();

        when(filmDao.getFilm(testId)).thenReturn(Optional.of(film2));

        assertEquals(film2, filmServiceDbImpl.findFilm(testId), "Фильм не совпадает с ожидаемым");

        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmServiceDbImpl.findFilm(wrongTestId));

        assertEquals("фильм с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Назначает id и добавляет в хранилище новый фильм. Если поле likes = null, то назначает ему пустой" +
            " HashSet. Возвращает добавленный фильм.")
    void shouldCreateNewFilm() throws Exception {
        final Film testFilm = new Film(0, "TestFilm11", "Description",
                LocalDate.parse("2019-04-12"), 186, null);
        final long expectedId = 11;
        final Film expectedFilm = new Film(expectedId, "TestFilm11", "Description",
                LocalDate.parse("2019-04-12"), 186, new HashSet<>());
        createTestFilms();

        final Film returnedFilm = filmServiceDbImpl.createFilm(testFilm);

        assertEquals(expectedId, returnedFilm.getId(), "id не соответствует ожидаемому");
        assertNotNull(returnedFilm.getLikes(), "Поле likes не должно быть null");
        assertEquals(expectedFilm, returnedFilm, "Возвращаемый фильм не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Обновляет фильм в хранилище, выбрасывает исключение если фильм с заданным id отсутствует." +
            " Возвращает измененный фильм")
    void shouldUpdateFilm() throws Exception {
        final long testId = 2;
        final long wrongTestId = 999;
        final Film expectedFilm = new Film(testId, "TestFilm2", "New description",
                LocalDate.parse("2020-06-01"), 195, new HashSet<>(List.of(1L, 2L, 5L)));
        final Film incorrectChangedTestFilm = new Film(wrongTestId, "TestFilm2", "New description",
                LocalDate.parse("2020-06-01"), 195, new HashSet<>());
        createTestFilms();

        final Film returnedFilm = filmServiceDbImpl.updateFilm(expectedFilm);

        assertNotNull(returnedFilm.getLikes(), "Поле likes не должно быть null");
        assertEquals(expectedFilm, returnedFilm, "Возвращаемый фильм не соответствует ожидаемому");

        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmServiceDbImpl.updateFilm(incorrectChangedTestFilm)
        );

        assertEquals("фильм с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Удаляет фильм из хранилища по заданному id, выбрасывает исключение если фильм с заданным id " +
            "отсутствует")
    void shouldDeleteFilmById() throws Exception {
        final long wrongTestId = 999;
        createTestFilms();

        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmServiceDbImpl.deleteFilm(wrongTestId)
        );

        assertEquals("фильм с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Добавляет лайк пользователя в список лайков фильма")
    void shouldAddLikeToFilmById() throws Exception {
        final Film testFilm = new Film(8, "TestFilm8", "Description",
                LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 4L, 6L)));
        final long userId = 6;
        createTestFilms();

        when(userStorage.getUser(userId)).thenReturn(Optional.of(new User(6, "User1Mail@google.com",
                "User6", "Ivan Ivanov", LocalDate.parse("1991-05-23"),
                new HashSet<>(List.of(4L)))));
        filmServiceDbImpl.addLike(testFilm.getId(), userId);

        assertTrue(filmDao.getFilm(testFilm.getId()).get().getLikes().contains(userId),
                "Лайк пользователя не добавлен в список лайков фильма");
    }

    @Test
    @DisplayName("Удаляет лайк пользователя из списка лайков фильма, выбрасывает исключение при отсутствии лайка" +
            "пользователя в списке лайков фильма")
    void shouldDeleteLikeOfFilmById() throws Exception {
        final Film testFilm = new Film(6, "TestFilm6", "Description",
                LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 2L, 3L, 4L)));
        final long userId = 5;
        createTestFilms();

        when(userStorage.getUser(userId)).thenReturn(Optional.of(new User(5, "User1Mail@google.com",
                "User6", "Ivan Ivanov", LocalDate.parse("1991-05-23"),
                new HashSet<>(List.of(4L)))));
        filmServiceDbImpl.deleteLike(testFilm.getId(), userId);

        assertFalse(filmDao.getFilm(testFilm.getId()).get().getLikes().contains(userId),
                "Лайк пользователя не удален из списка лайков фильма");

        final long wrongUserId = 8;
        when(userStorage.getUser(wrongUserId)).thenReturn(Optional.of(new User(8, "User1Mail@google.com",
                "User6", "Ivan Ivanov", LocalDate.parse("1991-05-23"),
                new HashSet<>(List.of(4L)))));

        final LikeNotFoundException e = assertThrows(
                LikeNotFoundException.class,
                () -> filmServiceDbImpl.deleteLike(testFilm.getId(), wrongUserId)
        );

        assertEquals("Лайк пользователя с заданным id не найден в списке лайков фильма " +
                testFilm.getId(), e.getMessage());
    }

    @Test
    @DisplayName("Возвращает отсортированный по количеству лайков список фильмов")
    void shouldReturnSortedByRatingFilms() throws Exception {
        final List<Film> expectedList = List.of(film6, film4, film10, film2, film5);
        createTestFilms();

        assertEquals(expectedList, filmServiceDbImpl.getPopularFilms(5), "Возвращенный список не " +
                "соответствует ожидаемому");
    }
}
