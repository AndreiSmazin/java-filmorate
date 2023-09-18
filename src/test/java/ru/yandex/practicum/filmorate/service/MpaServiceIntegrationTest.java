package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AllArgsConstructor
public abstract class MpaServiceIntegrationTest {
    protected final MpaService mpaService;

    @Test
    @DisplayName("Возвращает MPA по заданному id, выбрасывает исключение если MPA с заданным id отсутствует")
    public void shouldReturnMpaById() throws Exception {
        final int id = 4;
        final Mpa expectedMpa = new Mpa(4, "R");

        assertEquals(expectedMpa, mpaService.getMpaById(id), "MPA не совпадает с ожидаемым");

        final int wrongId = 10;
        IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> mpaService.getMpaById(wrongId));

        assertEquals("MPA с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Возвращает список всех MPA")
    public void shouldReturnAllMpa() throws Exception {
        final List<Mpa> expectedMpa = List.of(new Mpa(1, "G"),
                new Mpa(2, "PG"),
                new Mpa(3, "PG-13"),
                new Mpa(4, "R"),
                new Mpa(5, "NC-17"));

        assertEquals(expectedMpa, mpaService.getAllMpa(), "Список жанров не совпадает с ожидаемым");
    }
}
