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
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.service.impl.GenreServiceDbImpl;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {
    private static ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GenreServiceDbImpl genreService;

    @BeforeAll
    static void createMapper() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("GET /genres возвращает HTTP-ответ со статусом 200, типом данных application/json и списком жанров " +
            "в теле")
    void shouldReturnAllGenres() throws Exception {
        final Genre firstGenre = new Genre(1, "Комедия");
        final Genre secondGenre = new Genre(2, "Боевик");

        final List<Genre> genres = List.of(firstGenre, secondGenre);

        when(genreService.getAllGenres()).thenReturn(genres);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/genres"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(genres)));
    }

    @Test
    @DisplayName("GET /genres/{id} возвращает HTTP-ответ со статусом 200, типом данных application/json и жанром " +
            "в теле")
    void shouldReturnGenre() throws Exception {
        final Genre testGenre = new Genre(1, "Комедия");

        when(genreService.getGenreById(testGenre.getId())).thenReturn(testGenre);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/genres/" + testGenre.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(testGenre)));
    }
}
