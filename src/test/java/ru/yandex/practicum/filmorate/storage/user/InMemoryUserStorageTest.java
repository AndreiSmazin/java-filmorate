package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class InMemoryUserStorageTest {
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();

    final User firstUser = new User(1,"User1Mail@google.com", "User1", "Ivan Ivanov",
            LocalDate.parse("1991-05-23"));
    final User secondUser = new User(2,"User2Mail@google.com", "User2", "Petr Petrov",
            LocalDate.parse("1989-06-01"));

    private void createTestUsers() {
        userStorage.createNewUser(firstUser);
        userStorage.createNewUser(secondUser);
    }

    @Test
    @DisplayName("Возвращает список всех пользователей")
    void shouldReturnAllUsers() throws Exception {
        final List<User> expectedUsers = List.of(firstUser, secondUser);
        createTestUsers();

        assertEquals(expectedUsers, userStorage.findAllUsers(), "Список пользователей не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Возвращает пользователя по заданному id, выбрасывает исключение если пользователь с заданным id " +
            "отсутствует")
    void shouldReturnUserById() throws Exception {
        final long testId = 2;
        final long wrongTestId = 999;
        createTestUsers();

        assertEquals(secondUser, userStorage.findUser(testId), "Пользователь не совпадает с ожидаемым");

        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> userStorage.findUser(wrongTestId)
        );

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Добавляет в хранилище нового пользователя и возвращает его с назначенным id. Если поле name пустое, " +
            "то назначает ему значение login")
    void shouldCreateNewUser() throws Exception {
        final User thirdUser = new User(0,"User1Mail@google.com", "User3", "",
                LocalDate.parse("1991-05-23"));
        final long expectedId = 3;
        final User expectedUser = new User(3,"User1Mail@google.com", "User3", "User3",
                LocalDate.parse("1991-05-23"));
        createTestUsers();

        final User reternedUser = userStorage.createNewUser(thirdUser);

        assertEquals(expectedId, reternedUser.getId(), "Назначенный пользователю id не соответствует ожидаемому");
        assertEquals(reternedUser.getLogin(), reternedUser.getName(), "Назначенное пользователю name не " +
                "соответствует ожидаемому");
        assertEquals(expectedUser, userStorage.findUser(expectedId), "Пользователь не добавлен в хранилище");
        assertEquals(expectedUser, reternedUser, "Возвращенный пользователь не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Обновляет пользователя в хранилище и возвращает обновленного пользователя, выбрасывает исключение " +
            "если пользователь с заданным id отсутствует")
    void shouldUpdateUser() throws Exception {
        final long testId = 2;
        final long wrongTestId = 999;
        final User changedTestUser = new User(2,"ChangedMail@google.com", "User2", "Petr Petrov",
                LocalDate.parse("1988-06-01"));
        final User incorrectChangedTestUser = new User(wrongTestId,"ChangedMail@google.com", "User2",
                "Petr Petrov", LocalDate.parse("1988-06-01"));
        createTestUsers();

        final User reternedUser = userStorage.updateUser(changedTestUser);

        assertEquals(changedTestUser, userStorage.findUser(testId), "Пользователь не обновился в хранилище");
        assertEquals(changedTestUser, reternedUser, "Возвращенный пользователь не соответствует ожидаемому");

        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> userStorage.updateUser(incorrectChangedTestUser)
        );

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Удаляет всех пользователей")
    void shouldDeleteAllUsers() throws Exception {
        createTestUsers();

        userStorage.deleteAllUsers();

        assertTrue(userStorage.findAllUsers().isEmpty(), "Список пользователей не очищен");
    }

    @Test
    @DisplayName("Удаляет пользователя из хранилища по заданному id, выбрасывает исключение если пользователь с " +
            "заданным id отсутствует")
    void shouldDeleteUserById() throws Exception {
        final long testId = 2;
        final long wrongTestId = 999;
        createTestUsers();

        userStorage.deleteUser(testId);

        assertFalse(userStorage.findAllUsers().contains(secondUser), "Пользователь не удален");

        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> userStorage.deleteUser(wrongTestId)
        );

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }
}
