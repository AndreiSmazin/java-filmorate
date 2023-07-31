package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryFilmStorageTest {
    private final InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();

    final Film firstFilm = new Film(1, "TestFilm1", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>());
    final Film secondFilm = new Film(2, "TestFilm2", "Description",
            LocalDate.parse("2020-06-01"), 200, new HashSet<>());

    private void createTestFilms() {
        filmStorage.createNewFilm(firstFilm);
        filmStorage.createNewFilm(secondFilm);
    }

    @Test
    @DisplayName("Возвращает список всех фильмов")
    void shouldReturnAllFilms() throws Exception {
        final List<Film> expectedFilms = List.of(firstFilm, secondFilm);
        createTestFilms();

        assertEquals(expectedFilms, filmStorage.findAllFilms(), "Список фильмов не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Возвращает фильм по заданному id, выбрасывает исключение если фильм с заданным id отсутствует")
    void shouldReturnFilmById() throws Exception {
        final long testId = 2;
        final long wrongTestId = 999;
        createTestFilms();

        assertEquals(secondFilm, filmStorage.findFilm(testId), "Фильм не совпадает с ожидаемым");

        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmStorage.findFilm(wrongTestId)
        );

        assertEquals("фильм с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Добавляет в хранилище новый фильм и возвращает его с назначенным id. Если поле friends = null, то " +
            "назначает ему пустой HashSet")
    void shouldCreateNewFilm() throws Exception {
        final Film thirdFilm = new Film(0, "TestFilm3", "Description",
                LocalDate.parse("2019-04-12"), 186, null);
        final long expectedId = 3;
        final Film expectedFilm = new Film(expectedId, "TestFilm3", "Description",
                LocalDate.parse("2019-04-12"), 186, new HashSet<>());
        createTestFilms();

        final Film reternedFilm = filmStorage.createNewFilm(thirdFilm);

        assertEquals(expectedId, reternedFilm.getId(), "Назначенный фильму id не соответствует ожидаемому");
        assertNotNull(reternedFilm.getLikes(), "Поле likes не должно быть null");
        assertEquals(expectedFilm, filmStorage.findFilm(expectedId), "Фильм не добавлен в хранилище");
        assertEquals(expectedFilm, reternedFilm, "Возвращенный фильм не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Обновляет фильм в хранилище и возвращает обновленный фильм, выбрасывает исключение если фильм с " +
            "заданным id отсутствует")
    void shouldUpdateFilm() throws Exception {
        final long testId = 2;
        final long wrongTestId = 999;
        final Film changedTestFilm = new Film(testId, "TestFilm2", "New description",
                LocalDate.parse("2020-06-01"), 195, new HashSet<>());
        final Film incorrectChangedTestFilm = new Film(wrongTestId, "TestFilm2", "New description",
                LocalDate.parse("2020-06-01"), 195, new HashSet<>());
        createTestFilms();

        final Film reternedFilm = filmStorage.updateFilm(changedTestFilm);

        assertEquals(changedTestFilm, filmStorage.findFilm(testId), "Фильм не обновился в хранилище");
        assertEquals(changedTestFilm, reternedFilm, "Возвращенный фильм не соответствует ожидаемому");

        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmStorage.updateFilm(incorrectChangedTestFilm)
        );

        assertEquals("фильм с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Удаляет все фильмы")
    void shouldDeleteAllFilms() throws Exception {
        createTestFilms();

        filmStorage.deleteAllFilms();

        assertTrue(filmStorage.findAllFilms().isEmpty(), "Список фильмов не очищен");
    }

    @Test
    @DisplayName("Удаляет фильм из хранилища по заданному id, выбрасывает исключение если фильм с заданным id " +
            "отсутствует")
    void shouldDeleteFilmById() throws Exception {
        final long testId = 2;
        final long wrongTestId = 999;
        createTestFilms();

        filmStorage.deleteFilm(testId);

        assertFalse(filmStorage.findAllFilms().contains(secondFilm), "Фильм не удален");

        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmStorage.deleteFilm(wrongTestId)
        );

        assertEquals("фильм с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }
}
