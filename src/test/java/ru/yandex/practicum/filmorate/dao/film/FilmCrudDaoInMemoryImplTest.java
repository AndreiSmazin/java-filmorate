package ru.yandex.practicum.filmorate.dao.film;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.dao.impl.FilmCrudDaoInMemoryImpl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmCrudDaoInMemoryImplTest {
    private final FilmCrudDaoInMemoryImpl filmStorage = new FilmCrudDaoInMemoryImpl();

    final Film firstFilm = new Film(1, "TestFilm1", "Description",
            LocalDate.parse("1991-12-25"), 200, new HashSet<>());
    final Film secondFilm = new Film(2, "TestFilm2", "Description",
            LocalDate.parse("2020-06-01"), 200, new HashSet<>());

    private void createTestFilms() {
        filmStorage.addFilm(firstFilm);
        filmStorage.addFilm(secondFilm);
    }

    @Test
    @DisplayName("Возвращает список всех фильмов")
    void shouldReturnAllFilms() throws Exception {
        final List<Film> expectedFilmWithGenres = List.of(firstFilm, secondFilm);
        createTestFilms();

        assertEquals(expectedFilmWithGenres, filmStorage.getAllFilms(), "Список фильмов не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Возвращает фильм по заданному id")
    void shouldReturnFilmById() throws Exception {
        final long testId = 2;
        createTestFilms();

        assertEquals(secondFilm, filmStorage.getFilm(testId).get(), "Фильм не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Добавляет в хранилище новый фильм")
    void shouldCreateNewFilm() throws Exception {
        final Film thirdFilm = new Film(3, "TestFilm3", "Description",
                LocalDate.parse("2019-04-12"), 186, new HashSet<>());
        createTestFilms();

        filmStorage.addFilm(thirdFilm);

        assertEquals(thirdFilm, filmStorage.getFilm(thirdFilm.getId()).get(), "Фильм не добавлен в хранилище");
    }

    @Test
    @DisplayName("Обновляет фильм в хранилище")
    void shouldUpdateFilm() throws Exception {
        final Film testFilm = new Film(2, "TestFilm2", "New description",
                LocalDate.parse("2020-06-01"), 195, new HashSet<>());
        createTestFilms();

        filmStorage.updateFilm(testFilm);

        assertEquals(testFilm, filmStorage.getFilm(testFilm.getId()).get(), "Фильм не обновился в хранилище");
    }

    @Test
    @DisplayName("Удаляет все фильмы")
    void shouldDeleteAllFilms() throws Exception {
        createTestFilms();

        filmStorage.deleteAllFilms();

        assertTrue(filmStorage.getAllFilms().isEmpty(), "Список фильмов не очищен");
    }

    @Test
    @DisplayName("Удаляет фильм из хранилища по заданному id, возвращает удаленный фильм")
    void shouldDeleteFilmById() throws Exception {
        final long testId = 2;
        createTestFilms();

        final Film returnedFilm = filmStorage.deleteFilm(testId);

        assertFalse(filmStorage.getAllFilms().contains(secondFilm), "Фильм не удален");
        assertEquals(secondFilm, returnedFilm, "Возвращенный фильм не соответствует ожидаемому");
    }
}
