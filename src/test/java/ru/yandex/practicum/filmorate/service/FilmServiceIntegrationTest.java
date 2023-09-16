package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceIntegrationTest {
    private final FilmServiceDbImpl filmService;

    final Film testFilm1 = Film.builder().id(1)
            .name("Американский пирог")
            .description("Четверо друзей-школьников договариваются вместе сделать всё, чтобы лишиться девственности" +
                    " до конца их последнего учебного года в школе.")
            .releaseDate(LocalDate.parse("2000-01-20"))
            .duration(96)
            .mpa(new Mpa(4, "R"))
            .genres(List.of(new Genre(1, "Комедия")))
            .rate(0)
            .build();
    final Film testFilm2 = Film.builder().id(2)
            .name("Интерстеллар")
            .description("Группа ученых отправляются в космическое путешествие в поисках планеты с подходящими для" +
                    " человечества условиями.")
            .releaseDate(LocalDate.parse("2014-10-26"))
            .duration(169)
            .mpa(new Mpa(3, "PG-13"))
            .genres(List.of(new Genre(2, "Драма"), new Genre(7, "Фантастика"),
                    new Genre(8, "Приключения")))
            .rate(2)
            .build();
    final Film testFilm3 = Film.builder().id(3)
            .name("Крепкий орешек")
            .description("Полицейский Джон Макклейн ведет смертельную схватку с бандой политических террористов," +
                    " взявших в заложники два десятка человек, в числе которых его жена.")
            .releaseDate(LocalDate.parse("1988-07-12"))
            .duration(133)
            .mpa(new Mpa(4, "R"))
            .genres(List.of(new Genre(4, "Триллер"), new Genre(6, "Боевик"),
                    new Genre(9, "Криминал")))
            .rate(0)
            .build();
    final Film testFilm4 = Film.builder().id(4)
            .name("Ветер крепчает")
            .description("Дзиро с детства мечтал о полетах и красивых самолетах. Он начинает придумывать идеальный" +
                    " самолет и со временем становится одним из лучших авиаконструкторов мира.")
            .releaseDate(LocalDate.parse("2013-07-20"))
            .duration(126)
            .mpa(new Mpa(3, "PG-13"))
            .genres(List.of(new Genre(2, "Драма"), new Genre(3, "Мультфильм")))
            .rate(0)
            .build();

    @Test
    @DisplayName("Возвращает список всех фильмов")
    public void ShouldReturnAllFilms() throws Exception {
        final List<Film> expectedFilms = List.of(testFilm1, testFilm2, testFilm3);

        assertEquals(expectedFilms, filmService.findAllFilms(), "Список фильмов не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Возвращает фильм по заданному id, выбрасывает исключение если фильм с заданным id отсутствует")
    void shouldReturnFilmById() throws Exception {
        final int id = 2;
        final Film expectedFilm = testFilm2;

        assertEquals(expectedFilm, filmService.findFilm(id), "Фильм не совпадает с ожидаемым");

        final int wrongId = 34;
        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmService.findFilm(wrongId));

        assertEquals("фильм с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Добавляет в хранилище новый фильм. Возвращает добавленный фильм.")
    void shouldCreateNewFilm() throws Exception {
        final Film newFilm = testFilm4;
        final Film expectedFilm = testFilm4;

        assertEquals(expectedFilm, filmService.createFilm(newFilm), "Возвращаемый фильм не соответствует" +
                " ожидаемому");
        assertTrue(filmService.findAllFilms().contains(expectedFilm), "Фильм не добавлен в хранилище");
    }

    @Test
    @DisplayName("Обновляет фильм в хранилище, выбрасывает исключение если фильм с заданным id отсутствует." +
            " Возвращает измененный фильм")
    void shouldUpdateFilm() throws Exception {
        final Film modifiedFilm = Film.builder().id(1)
                .name("Круэлла")
                .description("Оставшись сиротой, Эстелла с друзьями промышляет на улицах британской столицы мелким" +
                        " воровством. Но девушка мечтает завязать и начать карьеру в мире моды.")
                .releaseDate(LocalDate.parse("2021-05-18"))
                .duration(134)
                .mpa(new Mpa(3, "PG-13"))
                .genres(List.of(new Genre(1, "Комедия"), new Genre(2, "Драма"),
                        new Genre(9, "Криминал")))
                .build();
        final Film expectedFilm = modifiedFilm;

        assertEquals(expectedFilm, filmService.updateFilm(modifiedFilm), "Возвращаемый фильм не соответствует" +
                " ожидаемому");
        assertEquals(modifiedFilm, filmService.findFilm(modifiedFilm.getId()), "Фильм не обновлен в хранилище");

        final Film wrongModifiedFilm = Film.builder().id(12)
                .name("Круэлла")
                .description("Оставшись сиротой, Эстелла с друзьями промышляет на улицах британской столицы мелким" +
                        " воровством. Но девушка мечтает завязать и начать карьеру в мире моды.")
                .releaseDate(LocalDate.parse("2021-05-18"))
                .duration(134)
                .mpa(new Mpa(3, "PG-13"))
                .genres(List.of(new Genre(1, "Комедия"), new Genre(2, "Драма"),
                        new Genre(9, "Криминал")))
                .build();
        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmService.updateFilm(wrongModifiedFilm));

        assertEquals("фильм с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Удаляет фильм из хранилища по заданному id, выбрасывает исключение если фильм с заданным id " +
            "отсутствует")
    void shouldDeleteFilmById() throws Exception {
        final int id = 2;

        filmService.deleteFilm(id);

        assertFalse(filmService.findAllFilms().contains(testFilm2), "Фильм не удален из хранилища");

        final int wrongId = 453;
        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmService.deleteFilm(wrongId));

        assertEquals("фильм с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Добавляет лайк пользователя в список лайков фильма,выбрасывает исключение если фильм или" +
            " пользователь с заданным id отсутствует")
    void shouldAddLikeToFilmById() throws Exception {
        final int filmId = 1;
        final int userId = 2;
        final int expectedRate = 1;

        filmService.addLike(filmId, userId);

        assertEquals(expectedRate, filmService.findFilm(filmId).getRate(), "Лайк не добавлен фильму");

        final int wrongFilmId = 13;
        IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmService.addLike(wrongFilmId, userId));

        assertEquals("фильм с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");

        final int wrongUserId = 28;
        e = assertThrows(
                IdNotFoundException.class,
                () -> filmService.addLike(filmId, wrongUserId));

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Удаляет лайк пользователя в список лайков фильма,выбрасывает исключение если фильм или" +
            " пользователь с заданным id отсутствует")
    void shouldDeleteLikeToFilmById() throws Exception {
        final int filmId = 2;
        final int userId = 1;
        final int expectedRate = 1;

        filmService.deleteLike(filmId, userId);

        assertEquals(expectedRate, filmService.findFilm(filmId).getRate(), "Лайк не удален");

        final int wrongFilmId = 25;
        IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmService.addLike(wrongFilmId, userId));

        assertEquals("фильм с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");

        final int wrongUserId = 59;
        e = assertThrows(
                IdNotFoundException.class,
                () -> filmService.addLike(filmId, wrongUserId));

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Возвращает список наиболее популярных фильмов")
    public void ShouldReturnPopularFilms() throws Exception {
        final List<Film> expectedFilms = List.of(testFilm2, testFilm1, testFilm3);
        int limit = 10;

        assertEquals(expectedFilms, filmService.getPopularFilms(limit), "Список фильмов не совпадает с" +
                " ожидаемым");

        final List<Film> otherExpectedFilms = List.of(testFilm2, testFilm1);
        limit = 2;
        assertEquals(otherExpectedFilms, filmService.getPopularFilms(limit), "Список фильмов не совпадает с" +
                " ожидаемым");
    }

    @Test
    @DisplayName("Возвращает жанр по заданному id, выбрасывает исключение если жанр с заданным id отсутствует")
    public void ShouldReturnGenreById() throws Exception {
        final int id = 5;
        final Genre expectedGenre = new Genre(5, "Документальный");

        assertEquals(expectedGenre, filmService.getGenreById(id), "Жанр не совпадает с ожидаемым");

        final int wrongId = 10;
        IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmService.getGenreById(wrongId));

        assertEquals("жанр с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Возвращает список всех жанров")
    public void ShouldReturnAllGenres() throws Exception {
        final List<Genre> expectedGenres = List.of(new Genre(1, "Комедия"),
                new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"),
                new Genre(4, "Триллер"),
                new Genre(5, "Документальный"),
                new Genre(6, "Боевик"),
                new Genre(7, "Фантастика"),
                new Genre(8, "Приключения"),
                new Genre(9, "Криминал"));


        assertEquals(expectedGenres, filmService.getAllGenres(), "Список жанров не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Возвращает MPA по заданному id, выбрасывает исключение если MPA с заданным id отсутствует")
    public void ShouldReturnMpaById() throws Exception {
        final int id = 4;
        final Mpa expectedMpa = new Mpa(4, "R");

        assertEquals(expectedMpa, filmService.getMpaById(id), "MPA не совпадает с ожидаемым");

        final int wrongId = 10;
        IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> filmService.getMpaById(wrongId));

        assertEquals("MPA с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Возвращает список всех MPA")
    public void ShouldReturnAllMpa() throws Exception {
        final List<Mpa> expectedMpa = List.of(new Mpa(1, "G"),
                new Mpa(2, "PG"),
                new Mpa(3, "PG-13"),
                new Mpa(4, "R"),
                new Mpa(5, "NC-17"));


        assertEquals(expectedMpa, filmService.getAllMpa(), "Список жанров не совпадает с ожидаемым");
    }
}
