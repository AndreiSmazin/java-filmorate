package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FilmServiceTest {
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final UserStorage userStorage = Mockito.mock(UserStorage.class);
    private final FilmService filmService = new FilmService(filmStorage, userStorage);

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
        filmStorage.createNewFilm(film1);
        filmStorage.createNewFilm(film2);
        filmStorage.createNewFilm(film3);
        filmStorage.createNewFilm(film4);
        filmStorage.createNewFilm(film5);
        filmStorage.createNewFilm(film6);
        filmStorage.createNewFilm(film7);
        filmStorage.createNewFilm(film8);
        filmStorage.createNewFilm(film9);
        filmStorage.createNewFilm(film10);
    }

    @Test
    @DisplayName("Добавляет лайк пользователя в список лайков фильма, возвращает фильм")
    void shouldAddLikeToFilmById() throws Exception {
        final Film expectedFilm = new Film(8, "TestFilm8", "Description",
                LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 4L, 6L)));
        final long userId = 6;
        createTestFilms();

        Mockito.when(userStorage.findUser(userId)).thenReturn(new User(6, "User1Mail@google.com",
                "User6", "Ivan Ivanov", LocalDate.parse("1991-05-23"),
                new HashSet<>(List.of(4L))));
        final Film result = filmService.addLike(expectedFilm.getId(), userId);

        assertTrue(filmStorage.findFilm(expectedFilm.getId()).getLikes().contains(userId),
                "Лайк пользователя не добавлен в список лайков фильма");
        assertEquals(expectedFilm, result, "Возвращенный фильм не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Удаляет лайк пользователя из списка лайков фильма, выбрасывает исключение при отсутствии лайка" +
            "пользователя в списке лайков фильма, возвращает фильм")
    void shouldDeleteLikeOfFilmById() throws Exception {
        final Film expectedFilm = new Film(6, "TestFilm6", "Description",
                LocalDate.parse("1991-12-25"), 200, new HashSet<>(List.of(1L, 2L, 3L, 4L)));
        final long userId = 5;
        createTestFilms();

        Mockito.when(userStorage.findUser(userId)).thenReturn(new User(5, "User1Mail@google.com",
                "User6", "Ivan Ivanov", LocalDate.parse("1991-05-23"),
                new HashSet<>(List.of(4L))));
        final Film result = filmService.deleteLike(expectedFilm.getId(), userId);

        assertFalse(filmStorage.findFilm(expectedFilm.getId()).getLikes().contains(userId),
                "Лайк пользователя не удален из списка лайков фильма");
        assertEquals(expectedFilm, result, "Возвращенный фильм не соответствует ожидаемому");

        final long wrongUserId = 8;
        Mockito.when(userStorage.findUser(wrongUserId)).thenReturn(new User(8, "User1Mail@google.com",
                "User6", "Ivan Ivanov", LocalDate.parse("1991-05-23"),
                new HashSet<>(List.of(4L))));

        final LikeNotFoundException e = assertThrows(
                LikeNotFoundException.class,
                () -> filmService.deleteLike(expectedFilm.getId(), wrongUserId)
        );

        assertEquals("Лайк пользователя с заданным id не найден в списке лайков фильма " +
                expectedFilm.getId(), e.getMessage());
    }

    @Test
    @DisplayName("Возвращает отсортированный по количеству лайков список фильмов")
    void shouldReturnSortedByRatingFilms() throws Exception {
        final List<Film> expectedList = List.of(film6, film4, film10, film2, film5);
        createTestFilms();

        assertEquals(expectedList, filmService.getPopularFilms(5), "Возвращенный список не " +
                "соответствует ожидаемому");
    }
}
