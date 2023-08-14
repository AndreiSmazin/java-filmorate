package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.impl.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryUserStorageTest {
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();

    final User firstUser = new User(1, "User1Mail@google.com", "User1", "Ivan Ivanov",
            LocalDate.parse("1991-05-23"), new HashSet<>());
    final User secondUser = new User(2, "User2Mail@google.com", "User2", "Petr Petrov",
            LocalDate.parse("1989-06-01"), new HashSet<>());

    private void createTestUsers() {
        userStorage.addUser(firstUser);
        userStorage.addUser(secondUser);
    }

    @Test
    @DisplayName("Возвращает список всех пользователей")
    void shouldReturnAllUsers() throws Exception {
        final List<User> expectedUsers = List.of(firstUser, secondUser);
        createTestUsers();

        assertEquals(expectedUsers, userStorage.getAllUsers(), "Список пользователей не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Возвращает пользователя по заданному id")
    void shouldReturnUserById() throws Exception {
        final long testId = 2;
        createTestUsers();

        assertEquals(secondUser, userStorage.getUser(testId).get(), "Пользователь не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Добавляет в хранилище нового пользователя")
    void shouldCreateNewUser() throws Exception {
        final User thirdUser = new User(3, "User1Mail@google.com", "User3", "",
                LocalDate.parse("1991-05-23"), new HashSet<>());
        createTestUsers();

        userStorage.addUser(thirdUser);

        assertEquals(thirdUser, userStorage.getUser(thirdUser.getId()).get(), "Пользователь не добавлен в" +
                " хранилище");
    }

    @Test
    @DisplayName("Обновляет пользователя в хранилище")
    void shouldUpdateUser() throws Exception {
        final User changedTestUser = new User(2, "ChangedMail@google.com", "User2", "Petr Petrov",
                LocalDate.parse("1988-06-01"), new HashSet<>());
        createTestUsers();

        userStorage.updateUser(changedTestUser);

        assertEquals(changedTestUser, userStorage.getUser(changedTestUser.getId()).get(), "Пользователь не" +
                " обновился в хранилище");
    }

    @Test
    @DisplayName("Удаляет всех пользователей")
    void shouldDeleteAllUsers() throws Exception {
        createTestUsers();

        userStorage.deleteAllUsers();

        assertTrue(userStorage.getAllUsers().isEmpty(), "Список пользователей не очищен");
    }

    @Test
    @DisplayName("Удаляет пользователя из хранилища по заданному id")
    void shouldDeleteUserById() throws Exception {
        final long testId = 2;
        createTestUsers();

        userStorage.deleteUser(testId);

        assertFalse(userStorage.getAllUsers().contains(secondUser), "Пользователь не удален");
    }
}
