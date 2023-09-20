package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.service.impl.FilmServiceDbImpl;
import ru.yandex.practicum.filmorate.valid.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.valid.Violation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {
    private static ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FilmServiceDbImpl filmService;

    @BeforeAll
    static void createMapper() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("GET /films возвращает HTTP-ответ со статусом 200, типом данных application/json и списком фильмов " +
            "в теле")
    void shouldReturnAllFilms() throws Exception {
        final Film firstFilm = new Film(1, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200, new Mpa(1, "R"), new ArrayList<>(), 0);
        final Film secondFilm = new Film(2, "TestFilm2", "Description",
                LocalDate.parse("2020-06-01"), 200, new Mpa(1, "R"), new ArrayList<>(), 0);

        final List<Film> films = List.of(firstFilm, secondFilm);

        when(filmService.findAllFilms()).thenReturn(films);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(films)));
    }

    @Test
    @DisplayName("GET /films/{id} возвращает HTTP-ответ со статусом 200, типом данных application/json и фильмом " +
            "в теле")
    void shouldReturnFilm() throws Exception {
        final Film testFilm = new Film(1, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200, new Mpa(1, "R"), new ArrayList<>(), 0);

        when(filmService.findFilm(testFilm.getId())).thenReturn(testFilm);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/films/" + testFilm.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(testFilm)));
    }

    @Test
    @DisplayName("POST /films возвращает HTTP-ответ со статусом 200 и фильмом в теле")
    void shouldCreateNewFilm() throws Exception {
        final Film testFilm = new Film(1, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200, new Mpa(1, "R"), new ArrayList<>(), 0);

        when(filmService.createFilm(testFilm)).thenReturn(testFilm);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testFilm)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(testFilm)));
    }

    @Test
    @DisplayName("POST /films при получении некорректных данных возвращает HTTP-ответ со статусом 400 и сообщениями " +
            "об ошибках валидации в теле")
    void shouldNotCreateNewFilmWhenDataIncorrect() throws Exception {
        final Film testFilm = new Film(1, "", "Description ......................................" +
                "..................................................................................................." +
                "......................................................................................... is to big",
                LocalDate.parse("1140-10-25"), -200, new Mpa(1, "R"), new ArrayList<>(), 0);
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
    }

    @Test
    @DisplayName("PUT /films возвращает HTTP-ответ со статусом 200 и фильмом в теле")
    void shouldUpdateFilm() throws Exception {
        final Film testFilm = new Film(1, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200, new Mpa(1, "R"), new ArrayList<>(), 0);

        when(filmService.updateFilm(testFilm)).thenReturn(testFilm);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testFilm)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(testFilm)));
    }

    @Test
    @DisplayName("PUT /films при получении некорректных данных возвращает HTTP-ответ со статусом 400 и сообщениями " +
            " об ошибках валидации в теле")
    void shouldNotUpdateFilmWhenDataIncorrect() throws Exception {
        final Film testFilm = new Film(1, "", "Description ..............................." +
                "..................................................................................................." +
                "......................................................................................... is to big",
                LocalDate.parse("1140-10-25"), -200, new Mpa(1, "R"), new ArrayList<>(), 0);
        final ValidationErrorResponse expectedResult = new ValidationErrorResponse(List.of(
                new Violation("name", "must not be blank"),
                new Violation("description", "size must be between 0 and 200"),
                new Violation("releaseDate", "не должна быть раньше 28 декабря 1895"),
                new Violation("duration", "must be greater than 0")));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testFilm)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("DELETE /films/{id} возвращает HTTP-ответ со статусом 200")
    void shouldDeleteFilm() throws Exception {
        final int testId = 1;

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/films/" + testId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT /films/{filmId}/like/{userId} возвращает HTTP-ответ со статусом 200")
    void shouldAddLikeToFilm() throws Exception {
        final int testFilmId = 1;
        final int userId = 10;

        this.mockMvc.perform(MockMvcRequestBuilders.put("/films/" + testFilmId + "/like/" + userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} возвращает HTTP-ответ со статусом 200")
    void shouldDeleteLikeOfFilm() throws Exception {
        final int testFilmId = 1;
        final long userId = 10;

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/films/" + testFilmId + "/like/" + userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} при получении некорректных id возвращает HTTP-ответ со статусом" +
            " 400 и сообщениями об ошибках валидации в теле")
    void shouldNotReturnPopularFilmsWhenCountIncorrect() throws Exception {
        final int count = 0;
        final ValidationErrorResponse expectedResult = new ValidationErrorResponse(List.of(
                new Violation("count", "must be greater than or equal to 1")));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/films/popular?count=" + count))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("GET /films/popular?count={count} возвращает HTTP-ответ со статусом 200")
    void shouldReturnPopularFilms() throws Exception {
        final Film firstFilm = new Film(1, "TestFilm1", "Description",
                LocalDate.parse("1991-12-25"), 200, new Mpa(1, "R"), new ArrayList<>(), 0);
        final Film secondFilm = new Film(2, "TestFilm2", "Description",
                LocalDate.parse("2020-06-01"), 200, new Mpa(1, "R"), new ArrayList<>(), 0);
        final int count = 2;

        final List<Film> films = List.of(firstFilm, secondFilm);

        when(filmService.getPopularFilms(count)).thenReturn(films);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/films/popular?count=" + count))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(films)));
    }
}

