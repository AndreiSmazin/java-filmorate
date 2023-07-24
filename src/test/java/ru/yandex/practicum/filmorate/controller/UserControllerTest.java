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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.valid.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.valid.Violation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserController userController;

    @BeforeAll
    static void createMapper() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @AfterEach
    void cleanFilms() {
        this.userController.getUsers().clear();
        this.userController.setCurrentID(1);
    }

    @Test
    @DisplayName("GET /users возвращает HTTP-ответ со статусом 200, типом данных application/json и ожидаемым списком пользователей")
    void shouldReturnAllFilms() throws Exception {
        final User firstUser = new User(1,"User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));
        final User secondUser = new User(2,"User2Mail@google.com", "User2", "Petr Petrov",
                LocalDate.parse("1989-06-01"));

        this.userController.getUsers().put(firstUser.getId(), firstUser);
        this.userController.getUsers().put(secondUser.getId(), secondUser);

        final List<User> expectedResult = new ArrayList<>(this.userController.getUsers().values());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("POST /users добавляет нового пользователя в список и возвращает HTTP-ответ со статусом 200, типом " +
            "данных application/json и пользователя с присвоенным id")
    void shouldCreateNewUser() throws Exception {
        final User testUser = new User(0,"User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));
        final User expectedResult = new User(1,"User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("POST /users не добавляет нового пользователя при получении неправильных данных и возвращает " +
            "HTTP-ответ со статусом 400 и сообщением об ошибках валидации")
    void shouldNotCreateNewUserWhenDataIncorrect() throws Exception {
        final User testUser = new User(0,"incorrect mail", "incorrect login", "Ivan Ivanov",
                LocalDate.now().plusDays(1));
        final ValidationErrorResponse expectedResult = new ValidationErrorResponse(List.of(
                new Violation("email", "must be a well-formed email address"),
                new Violation("login", "не должен быть пустым или содержать пробелы"),
                new Violation("birthday", "must be a past date")));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("POST /users добавляет нового пользователя в список при получении данных с полем name = null и " +
            "возвращает HTTP-ответ со статусом 200, типом данных application/json и пользователя с присвоенным id")
    void shouldCreateNewUserWithNullName() throws Exception {
        final User testUser = new User(0,"User1Mail@google.com", "User1", null,
                LocalDate.parse("1991-05-23"));
        final User expectedResult = new User(1,"User1Mail@google.com", "User1", "User1",
                LocalDate.parse("1991-05-23"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("PUT /users обновляет пользователя и возвращает HTTP-ответ со статусом 200, типом данных " +
            "application/json и обновленным пользователем")
    void shouldUpdateUser() throws Exception {
        final User testUser = new User(1,"User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));

        this.userController.getUsers().put(testUser.getId(), testUser);

        final User changedTestUser = new User(1,"ChangedUser1Mail@google.com", "ChangedUser1", "Ivan Petrov",
                LocalDate.parse("1991-05-26"));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changedTestUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(changedTestUser)));
    }

    @Test
    @DisplayName("PUT /users не обновляет пользователя при получении неправильных данных и возвращает HTTP-ответ " +
            "со статусом 400 и сообщением об ошибках валидации")
    void shouldNotUpdateUserWhenDataIncorrect() throws Exception {
        final User testUser = new User(1,"User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));

        this.userController.getUsers().put(testUser.getId(), testUser);

        final User changedTestUser = new User(0,"incorrect mail", "incorrect login", "Ivan Ivanov",
                LocalDate.now().plusDays(1));
        final ValidationErrorResponse expectedResult = new ValidationErrorResponse(List.of(
                new Violation("email", "must be a well-formed email address"),
                new Violation("login", "не должен быть пустым или содержать пробелы"),
                new Violation("birthday", "must be a past date")));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changedTestUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("PUT /users обновляет пользователя при получении данных с полем name = null и возвращает HTTP-ответ " +
            "со статусом 200, типом данных application/json и обновленным пользователем")
    void shouldUpdateUserWithNullName() throws Exception {
        final User testUser = new User(1,"User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));

        this.userController.getUsers().put(testUser.getId(), testUser);

        final User changedTestUser = new User(1,"ChangedUser1Mail@google.com", "ChangedUser1", null,
                LocalDate.parse("1991-05-26"));
        final User expectedResult = new User(1,"ChangedUser1Mail@google.com", "ChangedUser1",
                "ChangedUser1", LocalDate.parse("1991-05-26"));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changedTestUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("PUT /users не обновляет пользователя при получении некорректного id и возвращает HTTP-ответ " +
            "со статусом 404 и сообщением об ошибке")
    void shouldNotUpdateUserWhenIdNotExist() throws Exception {
        final User testUser = new User(1,"User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));

        this.userController.getUsers().put(testUser.getId(), testUser);

        final User changedTestUser = new User(2,"User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));
        final Violation expectedResult = new Violation("id", "пользователь с заданным id не найден");

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changedTestUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));
    }
}