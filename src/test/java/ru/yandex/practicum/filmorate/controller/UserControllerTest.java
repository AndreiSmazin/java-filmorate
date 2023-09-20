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
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.impl.UserServiceDbImpl;
import ru.yandex.practicum.filmorate.valid.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.valid.Violation;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    private static ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceDbImpl userService;

    @BeforeAll
    static void createMapper() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("GET /users возвращает HTTP-ответ со статусом 200, типом данных application/json и списком " +
            "пользователей в теле")
    void shouldReturnAllUsers() throws Exception {
        final User firstUser = new User(1, "User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));
        final User secondUser = new User(2, "User2Mail@google.com", "User2", "Petr Petrov",
                LocalDate.parse("1989-06-01"));

        final List<User> users = List.of(firstUser, secondUser);

        when(userService.findAllUsers()).thenReturn(users);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(users)));
    }

    @Test
    @DisplayName("GET /users/{id} возвращает HTTP-ответ со статусом 200, типом данных application/json и " +
            "пользователем в теле")
    void shouldReturnUser() throws Exception {
        final User testUser = new User(1, "User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));

        when(userService.findUser(1)).thenReturn(testUser);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/" + testUser.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(testUser)));
    }

    @Test
    @DisplayName("POST /users возвращает HTTP-ответ со статусом 200 и пользователем в теле")
    void shouldCreateNewUser() throws Exception {
        final User testUser = new User(0, "User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));

        when(userService.createUser(testUser)).thenReturn(testUser);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(testUser)));
    }

    @Test
    @DisplayName("POST /users при получении некорректных данных возвращает HTTP-ответ со статусом 400 и сообщениями " +
            "об ошибках валидации в теле")
    void shouldNotCreateNewUserWhenDataIncorrect() throws Exception {
        final User testUser = new User(0, "incorrect mail", "incorrect login", "Ivan Ivanov",
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
    @DisplayName("PUT /users возвращает HTTP-ответ со статусом 200 и пользователем в теле")
    void shouldUpdateUser() throws Exception {
        final User testUser = new User(1, "User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));

        when(userService.updateUser(testUser)).thenReturn(testUser);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(testUser)));
    }

    @Test
    @DisplayName("PUT /users при получении некорректных данных возвращает HTTP-ответ со статусом 400 и сообщениями " +
            "об ошибках валидации в теле")
    void shouldNotUpdateUserWhenDataIncorrect() throws Exception {
        final User testUser = new User(0, "incorrect mail", "incorrect login", "Ivan Ivanov",
                LocalDate.now().plusDays(1));
        final ValidationErrorResponse expectedResult = new ValidationErrorResponse(List.of(
                new Violation("email", "must be a well-formed email address"),
                new Violation("login", "не должен быть пустым или содержать пробелы"),
                new Violation("birthday", "must be a past date")));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("DELETE /users/{id} возвращает HTTP-ответ со статусом 200")
    void shouldDeleteUser() throws Exception {
        final int testId = 1;

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + testId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT /users/{userId}/friends/{friendId} возвращает HTTP-ответ со статусом 200")
    void shouldAddNewFriend() throws Exception {
        final int testUserId = 1;
        final int testFriendId = 4;

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users/" + testUserId + "/friends/" +
                        testFriendId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("DELETE /users/{userId}/friends/{friendId} возвращает HTTP-ответ со статусом 200")
    void shouldDeleteFriend() throws Exception {
        final int testUserId = 1;
        final int testFriendId = 4;

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + testUserId + "/friends/" +
                        testFriendId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /users/{userId}/friends возвращает HTTP-ответ со статусом 200, типом данных application/json и " +
            "списком друзей пользователя в теле")
    void shouldReturnFriends() throws Exception {
        final int testId = 3;
        final User firstUser = new User(1, "User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));
        final User secondUser = new User(2, "User2Mail@google.com", "User2", "Petr Petrov",
                LocalDate.parse("1989-06-01"));

        final List<User> friends = List.of(firstUser, secondUser);

        when(userService.findFriends(testId)).thenReturn(friends);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/" + testId + "/friends"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(friends)));
    }

    @Test
    @DisplayName("GET /users/{userId}/friends/common/{otherUserId} возвращает HTTP-ответ со статусом 200, типом" +
            " данных application/json и списком общих друзей пользователя в теле")
    void shouldReturnCommonFriends() throws Exception {
        final int firstTestId = 3;
        final int secondTestId = 4;
        final User firstUser = new User(1, "User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"));
        final User secondUser = new User(2, "User2Mail@google.com", "User2", "Petr Petrov",
                LocalDate.parse("1989-06-01"));

        final List<User> friends = List.of(firstUser, secondUser);

        when(userService.findCommonFriends(firstTestId, secondTestId)).thenReturn(friends);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/" + firstTestId + "/friends/common/" +
                        secondTestId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(friends)));
    }
}