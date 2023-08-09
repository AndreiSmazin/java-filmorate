package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.impl.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    @DisplayName("Возвращает фильм по заданному id, возвращает пустой Optional фильм с заданным id отсутствует")
    void shouldReturnFilmById() throws Exception {
        final long testId = 2;
        final long wrongTestId = 999;
        createTestFilms();

        assertEquals(secondFilm, filmStorage.findFilm(testId).get(), "Фильм не совпадает с ожидаемым");
        assertEquals(Optional.empty(), filmStorage.findFilm(wrongTestId), "Возвращаемый результат не" +
                " пустой");
    }

    @Test
    @DisplayName("Добавляет в хранилище новый фильм. Если поле friends = null, то назначает ему пустой HashSet." +
            " Возвращает добавленный фильм.")
    void shouldCreateNewFilm() throws Exception {
        final Film thirdFilm = new Film(0, "TestFilm3", "Description",
                LocalDate.parse("2019-04-12"), 186, null);
        final long expectedId = 3;
        final Film expectedFilm = new Film(expectedId, "TestFilm3", "Description",
                LocalDate.parse("2019-04-12"), 186, new HashSet<>());
        createTestFilms();

        final Film returnedFilm = filmStorage.createNewFilm(thirdFilm);
        final Film newFilm = filmStorage.findFilm(expectedId).get();

        assertNotNull(newFilm.getLikes(), "Поле likes не должно быть null");
        assertEquals(expectedFilm, newFilm, "Фильм не добавлен в хранилище");
        assertEquals(expectedFilm, returnedFilm, "Возвращаемый фильм не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Обновляет фильм в хранилище, выбрасывает исключение если фильм с заданным id отсутствует." +
            " Возвращает измененный фильм")
    void shouldUpdateFilm() throws Exception {
        final long testId = 2;
        final long wrongTestId = 999;
        final Film expectedFilm = new Film(testId, "TestFilm2", "New description",
                LocalDate.parse("2020-06-01"), 195, new HashSet<>());
        final Film incorrectChangedTestFilm = new Film(wrongTestId, "TestFilm2", "New description",
                LocalDate.parse("2020-06-01"), 195, new HashSet<>());
        createTestFilms();

        final Film returnedFilm = filmStorage.updateFilm(expectedFilm);

        assertEquals(expectedFilm, filmStorage.findFilm(testId).get(), "Фильм не обновился в хранилище");
        assertEquals(expectedFilm, returnedFilm, "Возвращаемый фильм не соответствует ожидаемому");

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
