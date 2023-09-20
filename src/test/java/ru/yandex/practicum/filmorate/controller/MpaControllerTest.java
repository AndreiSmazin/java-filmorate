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
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.service.impl.MpaServiceDbImpl;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(MpaController.class)
public class MpaControllerTest {
    private static ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MpaServiceDbImpl mpaService;

    @BeforeAll
    static void createMapper() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("GET /mpa возвращает HTTP-ответ со статусом 200, типом данных application/json и списком MPA " +
            "в теле")
    void shouldReturnAllMpa() throws Exception {
        final Mpa firstMpa = new Mpa(1, "R");
        final Mpa secondMpa = new Mpa(2, "PG-13");

        final List<Mpa> mpa = List.of(firstMpa, secondMpa);

        when(mpaService.getAllMpa()).thenReturn(mpa);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/mpa"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(mpa)));
    }

    @Test
    @DisplayName("GET /mpa/{id} возвращает HTTP-ответ со статусом 200, типом данных application/json и MPA " +
            "в теле")
    void shouldReturnMpa() throws Exception {
        final Mpa testMpa = new Mpa(1, "R");

        when(mpaService.getMpaById(testMpa.getId())).thenReturn(testMpa);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/mpa/" + testMpa.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(testMpa)));
    }
}
