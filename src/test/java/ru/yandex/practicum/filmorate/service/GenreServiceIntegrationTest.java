package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AllArgsConstructor
public abstract class GenreServiceIntegrationTest {
    protected final GenreService genreService;

    @Test
    @DisplayName("Возвращает жанр по заданному id, выбрасывает исключение если жанр с заданным id отсутствует")
    public void shouldReturnGenreById() throws Exception {
        final int id = 5;
        final Genre expectedGenre = new Genre(5, "Документальный");

        assertEquals(expectedGenre, genreService.getGenreById(id), "Жанр не совпадает с ожидаемым");

        final int wrongId = 10;
        IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> genreService.getGenreById(wrongId));

        assertEquals("жанр с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Возвращает список всех жанров")
    public void shouldReturnAllGenres() throws Exception {
        final List<Genre> expectedGenres = List.of(new Genre(1, "Комедия"),
                new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"),
                new Genre(4, "Триллер"),
                new Genre(5, "Документальный"),
                new Genre(6, "Боевик"));


        assertEquals(expectedGenres, genreService.getAllGenres(), "Список жанров не совпадает с ожидаемым");
    }
}
