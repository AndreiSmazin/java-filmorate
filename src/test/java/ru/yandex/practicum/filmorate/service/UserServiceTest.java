package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectFriendIdException;
import ru.yandex.practicum.filmorate.entity.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final UserServiceAbstractImpl userServiceAbstractImpl = new UserServiceAbstractImpl(userStorage);

    final User firstUser = new User(1, "User1Mail@google.com", "User1", "Ivan Ivanov",
            LocalDate.parse("1991-05-23"), new HashSet<>(List.of(3L, 4L)));
    final User secondUser = new User(2, "User2Mail@google.com", "User2", "Petr Petrov",
            LocalDate.parse("1989-06-01"), new HashSet<>());
    final User thirdUser = new User(3, "User3Mail@google.com", "User3", "Maria Petrova",
            LocalDate.parse("1978-12-10"), new HashSet<>(List.of(1L, 4L)));
    final User fourthUser = new User(4, "User4Mail@google.com", "User4", "Irina Stepanova",
            LocalDate.parse("1996-10-12"), new HashSet<>(List.of(1L, 3L)));

    private void createTestUsers() {
        userServiceAbstractImpl.createUser(firstUser);
        userServiceAbstractImpl.createUser(secondUser);
        userServiceAbstractImpl.createUser(thirdUser);
        userServiceAbstractImpl.createUser(fourthUser);
    }

    @Test
    @DisplayName("Возвращает пользователя по заданному id, выбрасывает исключение если пользователь с заданным id " +
            "отсутствует")
    void shouldReturnUserById() throws Exception {
        final long testId = 2;
        final long wrongTestId = 999;
        createTestUsers();

        assertEquals(secondUser, userServiceAbstractImpl.findUser(2), "Пользователь не совпадает с ожидаемым");
        assertEquals(Optional.empty(), userStorage.getUser(wrongTestId), "Возвращаемый результат не" +
                " пустой");
    }

    @Test
    @DisplayName("Добавляет в хранилище нового пользователя. Если поле name пустое, то назначает ему значение login." +
            " Если поле friends = null, то назначает ему пустой HashSet. Возвращает нового пользователя")
    void shouldCreateNewUser() throws Exception {
        final User testUser = new User(0, "User1Mail@google.com", "User5", "",
                LocalDate.parse("1991-05-23"), null);
        final User expectedUser = new User(5, "User1Mail@google.com", "User5", "User5",
                LocalDate.parse("1991-05-23"), new HashSet<>());
        createTestUsers();

        final User returnedUser = userServiceAbstractImpl.createUser(testUser);

        assertEquals(returnedUser.getLogin(), returnedUser.getName(), "Назначенное пользователю name не " +
                "соответствует ожидаемому");
        assertNotNull(returnedUser.getFriends(), "Поле friends не должно быть null");
        assertEquals(expectedUser, returnedUser, "Возвращенный пользователь не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Обновляет пользователя в хранилище, выбрасывает исключение если пользователь с заданным id" +
            " отсутствует. Возвращает обновленного пользователя")
    void shouldUpdateUser() throws Exception {
        final long wrongTestId = 999;
        final User changedTestUser = new User(2, "ChangedMail@google.com", "User2", "Petr Petrov",
                LocalDate.parse("1988-06-01"), new HashSet<>());
        final User incorrectChangedTestUser = new User(wrongTestId, "ChangedMail@google.com", "User2",
                "Petr Petrov", LocalDate.parse("1988-06-01"), new HashSet<>());
        createTestUsers();

        final User returnedUser = userServiceAbstractImpl.updateUser(changedTestUser);

        assertEquals(changedTestUser, returnedUser, "Возвращенный пользователь не соответствует ожидаемому");

        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> userServiceAbstractImpl.updateUser(incorrectChangedTestUser)
        );

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Удаляет пользователя из хранилища по заданному id, выбрасывает исключение если пользователь с " +
            "заданным id отсутствует")
    void shouldDeleteUserById() throws Exception {
        final long testId = 4;
        final long wrongTestId = 999;
        createTestUsers();

        userServiceAbstractImpl.deleteUser(testId);

        assertFalse(userStorage.getUser(1).get().getFriends().contains(testId), "пользователь не удален" +
                " из друзей пользователя 1");
        assertFalse(userStorage.getUser(3).get().getFriends().contains(testId), "пользователь не удален" +
                " из друзей пользователя 3");

        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> userServiceAbstractImpl.deleteUser(wrongTestId)
        );

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Добавляет пользователя 1 в список друзей пользователя 2 и пользователя 2 в список друзей " +
            "пользователя 1, выбрасывает исключение при попытке добавить пользователя в собственный список друзей, " +
            "возвращает пользователя 1")
    void shouldAddFriendToUserById() throws Exception {
        final User expectedFirstUser = new User(1, "User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"), new HashSet<>(List.of(3L, 4L, 2L)));
        final long secondUserId = 2;
        createTestUsers();

        userServiceAbstractImpl.addFriend(expectedFirstUser.getId(), secondUserId);

        assertTrue(userStorage.getUser(expectedFirstUser.getId()).get().getFriends().contains(secondUserId),
                "Пользователь 2 не добавлен в список друзей пользователя 1");
        assertTrue(userStorage.getUser(secondUserId).get().getFriends().contains(expectedFirstUser.getId()),
                "Пользователь 1 не добавлен в список друзей пользователя 2");

        final IncorrectFriendIdException e = assertThrows(
                IncorrectFriendIdException.class,
                () -> userServiceAbstractImpl.addFriend(expectedFirstUser.getId(), expectedFirstUser.getId())
        );

        assertEquals("id пользователей совпадают", e.getMessage(), "Сообщение об исключении не " +
                "соответствует ожидаемому");
    }

    @Test
    @DisplayName("Удаляет пользователя 2 из списка друзей пользователя 1 и пользователя 1 из списка друзей " +
            "пользователя 2, выбрасывает исключение при отсутствии пользователя 2 в списке друзей пользователя 1, " +
            "возвращает пользователя 1")
    void shouldDeleteFriendOfUserById() throws Exception {
        final User expectedFirstUser = new User(1, "User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"), new HashSet<>(List.of(4L)));
        final long secondUserId = 3;
        createTestUsers();

        userServiceAbstractImpl.deleteFriend(expectedFirstUser.getId(), secondUserId);

        assertFalse(userStorage.getUser(expectedFirstUser.getId()).get().getFriends().contains(secondUserId),
                "Пользователь 2 не удален из списка друзей пользователя 1");
        assertFalse(userStorage.getUser(secondUserId).get().getFriends().contains(expectedFirstUser.getId()),
                "Пользователь 1 не удален из списка друзей пользователя 2");

        final long wrongTestFriendId = 2;
        final FriendNotFoundException e = assertThrows(
                FriendNotFoundException.class,
                () -> userServiceAbstractImpl.deleteFriend(expectedFirstUser.getId(), wrongTestFriendId)
        );

        assertEquals("пользователь с заданным id не найден в списке друзей пользователя " +
                expectedFirstUser.getId(), e.getMessage(), "Сообщение об исключении не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Возвращает список друзей пользователя")
    void shouldReturnFriendsOfUserById() throws Exception {
        final long testUserId = 1;
        final List<User> expectedFriends = List.of(thirdUser, fourthUser);
        createTestUsers();

        assertEquals(expectedFriends, userServiceAbstractImpl.findFriends(testUserId), "Список друзей не совпадает с" +
                "ожидаемым");
    }

    @Test
    @DisplayName("Возвращает список общих друзей двоих пользователей")
    void shouldReturnCommonFriendsOfUsersById() throws Exception {
        final long firstUserId = 3;
        final long secondUserId = 4;
        final List<User> expectedCommonFriends = List.of(firstUser);
        createTestUsers();

        assertEquals(expectedCommonFriends, userServiceAbstractImpl.findCommonFriends(firstUserId, secondUserId), "Список" +
                " общих друзей не совпадает с ожидаемым");

    }
}
