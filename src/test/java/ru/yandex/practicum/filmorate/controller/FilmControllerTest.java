package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.model.Violation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {
    private static ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmController filmController;

    @BeforeAll
    static void createMapper() {
        mapper = new ObjectMapper() .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @AfterEach
    void cleanFilms() {
        this.filmController.getFilms().clear();
    }

    @Test
    @DisplayName("GET /films возвращает HTTP-ответ со статусом 200, типом данных application/json и ожидаемым списком фильмов")
    void shouldReturnAllFilms() throws Exception {
        final Film firstFilm = new Film(1, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200);
        final Film secondFilm = new Film(2, "TestFilm2", "Description",
                LocalDate.parse("2020-06-01"), 200);

        this.filmController.getFilms().put(firstFilm.getId(), firstFilm);
        this.filmController.getFilms().put(secondFilm.getId(), secondFilm);

        final List<Film> expectedResult = new ArrayList<>(this.filmController.getFilms().values());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("POST /films добавляет новый фильм в список и возвращает HTTP-ответ со статусом 200, типом данных " +
            "application/json и фильмом с присвоенным id")
    void shouldCreateNewFilm() throws Exception {
        final Film testFilm = new Film(0, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200);
        final Film expectedResult = new Film(1, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testFilm)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));

        assertEquals(1, this.filmController.getFilms().size());
    }

    @Test
    @DisplayName("POST /films не добавляет новый фильм при получении неправильных данных и возвращает HTTP-ответ " +
            "со статусом 400 и сообщением об ошибках валидации")
    void shouldNotCreateNewFilmWhenDataIncorrect() throws Exception {
        final Film testFilm = new Film(0, "", "Description ......................................" +
                "..................................................................................................." +
                "......................................................................................... is to big",
                LocalDate.parse("1140-10-25"), -200);
        final ValidationErrorResponse expectedResult = new ValidationErrorResponse(List.of(
                new Violation("name", "must not be blank"),
                new Violation("description", "size must be between 0 and 200"),
                new Violation("releaseDate", "не должна быть раньше 28 декабря 1895"),
                new Violation("duration", "must be greater than 0")));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testFilm)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));

        assertTrue(this.filmController.getFilms().isEmpty());
    }

    @Test
    @DisplayName("PUT /films обновляет фильм и возвращает HTTP-ответ со статусом 200, типом данных " +
            "application/json и обновленным фильмом")
    void shouldUpdateFilm() throws Exception {
        final Film testFilm = new Film(1, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200);

        this.filmController.getFilms().put(testFilm.getId(), testFilm);

        final Film changedTestFilm = new Film(1, "TestFilm1, changed", "Description, changed",
                LocalDate.parse("2005-03-12"), 210);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changedTestFilm)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(changedTestFilm)));

        assertEquals(changedTestFilm, this.filmController.getFilms().get(testFilm.getId()));
    }

    @Test
    @DisplayName("PUT /films не обновляет фильм при получении неправильных данных и возвращает HTTP-ответ " +
            "со статусом 400 и сообщением об ошибках валидации")
    void shouldNotUpdateFilmWhenDataIncorrect() throws Exception {
        final Film testFilm = new Film(1, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200);

        this.filmController.getFilms().put(testFilm.getId(), testFilm);

        final Film changedTestFilm = new Film(1, "", "Description ..............................." +
                "..................................................................................................." +
                "......................................................................................... is to big",
                LocalDate.parse("1140-10-25"), -200);
        final ValidationErrorResponse expectedResult = new ValidationErrorResponse(List.of(
                new Violation("name", "must not be blank"),
                new Violation("description", "size must be between 0 and 200"),
                new Violation("releaseDate", "не должна быть раньше 28 декабря 1895"),
                new Violation("duration", "must be greater than 0")));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changedTestFilm)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));

        assertNotEquals(changedTestFilm, this.filmController.getFilms().get(testFilm.getId()));
    }

    @Test
    @DisplayName("PUT /films не обновляет фильм при получении некорректного id и возвращает HTTP-ответ " +
            "со статусом 404 и сообщением об ошибке")
    void shouldNotUpdateFilmWhenIdNotExist() throws Exception {
        final Film testFilm = new Film(1, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200);

        this.filmController.getFilms().put(testFilm.getId(), testFilm);

        final Film changedTestFilm = new Film(2, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200);
        final Violation expectedResult = new Violation("id", "фильм с заданным id не найден");

        this.mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changedTestFilm)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));

        assertEquals(1, this.filmController.getFilms().size());
    }
}
