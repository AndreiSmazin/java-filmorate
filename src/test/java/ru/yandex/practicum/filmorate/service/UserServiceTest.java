package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectFriendIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserServiceTest {
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final UserService userService = new UserService(userStorage);

    final User firstUser = new User(1,"User1Mail@google.com", "User1", "Ivan Ivanov",
            LocalDate.parse("1991-05-23"), new HashSet<>(List.of(3l, 4l)));
    final User secondUser = new User(2,"User2Mail@google.com", "User2", "Petr Petrov",
            LocalDate.parse("1989-06-01"), new HashSet<>());
    final User thirdUser = new User(3,"User3Mail@google.com", "User3", "Maria Petrova",
            LocalDate.parse("1978-12-10"), new HashSet<>(List.of(1l, 4l)));
    final User fourthUser = new User(4,"User4Mail@google.com", "User4", "Irina Stepanova",
            LocalDate.parse("1996-10-12"), new HashSet<>(List.of(1l, 3l)));

    private void createTestUsers() {
        userStorage.createNewUser(firstUser);
        userStorage.createNewUser(secondUser);
        userStorage.createNewUser(thirdUser);
        userStorage.createNewUser(fourthUser);
    }

    @Test
    @DisplayName("Добавляет пользователя 1 в список друзей пользователя 2 и пользователя 2 в список друзей " +
            "пользователя 1, выбрасывает исключение при попытке добавить пользователя в собственный список друзей, " +
            "возвращает пользователя 1")
    void shouldAddFriendToUserById() throws Exception {
        final User expectedFirstUser = new User(1,"User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"), new HashSet<>(List.of(3l, 4l, 2l)));
        final long secondUserId = 2;
        createTestUsers();

        final User result = userService.addFriend(expectedFirstUser.getId(), secondUserId);

        assertTrue(userStorage.findUser(expectedFirstUser.getId()).getFriends().contains(secondUserId),
                "Пользователь 2 не добавлен в список друзей пользователя 1");
        assertTrue(userStorage.findUser(secondUserId).getFriends().contains(expectedFirstUser.getId()),
                "Пользователь 1 не добавлен в список друзей пользователя 2");
        assertEquals(expectedFirstUser, result, "Возвращенный пользователь не соответствует ожидаемому");

        final IncorrectFriendIdException e = assertThrows(
                IncorrectFriendIdException.class,
                () -> userService.addFriend(expectedFirstUser.getId(), expectedFirstUser.getId())
        );

        assertEquals("id пользователя и id друга совпадают",
                e.getMessage());
    }

    @Test
    @DisplayName("Удаляет пользователя 2 из списка друзей пользователя 1 и пользователя 1 из списка друзей " +
            "пользователя 2, выбрасывает исключение при отсутствии пользователя 2 в списке друзей пользователя 1, " +
            "возвращает пользователя 1")
    void shouldDeleteFriendOfUserById() throws Exception {
        final User expectedFirstUser = new User(1,"User1Mail@google.com", "User1", "Ivan Ivanov",
                LocalDate.parse("1991-05-23"), new HashSet<>(List.of(4l)));
        final long secondUserId = 3;
        createTestUsers();

        final User result = userService.deleteFriend(expectedFirstUser.getId(), secondUserId);

        assertFalse(userStorage.findUser(expectedFirstUser.getId()).getFriends().contains(secondUserId),
                "Пользователь 2 не удален из списка друзей пользователя 1");
        assertFalse(userStorage.findUser(secondUserId).getFriends().contains(expectedFirstUser.getId()),
                "Пользователь 1 не удален из списка друзей пользователя 2");
        assertEquals(expectedFirstUser, result, "Возвращенный пользователь не соответствует ожидаемому");

        final long wrongTestFriendId = 2;
        final FriendNotFoundException e = assertThrows(
                FriendNotFoundException.class,
                () -> userService.deleteFriend(expectedFirstUser.getId(), wrongTestFriendId)
        );

        assertEquals("пользователь с заданным id не найден в списке друзей пользователя " +
                expectedFirstUser.getId(), e.getMessage());
    }

    @Test
    @DisplayName("Очищает список друзей пользователя, удаляет пользователя из списков друзей других пользователей ")
    void shouldDeleteAllFriendsOfUserById() throws Exception {
        final long firstUserId = 1;
        final long secondUserId = 3;
        final long thirdUserId = 4;
        createTestUsers();

        userService.deleteAllFriends(firstUserId);

        assertTrue(userStorage.findUser(firstUserId).getFriends().isEmpty(), "Список друзей пользователя 1 не " +
                "очищен");
        assertFalse(userStorage.findUser(secondUserId).getFriends().contains(firstUserId),
                "Пользователь 1 не удален из списка друзей пользователя 2");
        assertFalse(userStorage.findUser(thirdUserId).getFriends().contains(firstUserId),
                "Пользователь 1 не удален из списка друзей пользователя 3");
    }

    @Test
    @DisplayName("Возвращает список друзей пользователя")
    void shouldReturnFriendsOfUserById() throws Exception {
        final long testUserId = 1;
        final List<User> expectedFriends = List.of(thirdUser, fourthUser);
        createTestUsers();

        assertEquals(expectedFriends, userService.findFriends(testUserId), "Список друзей не совпадает с" +
                "ожидаемым");
    }

    @Test
    @DisplayName("Возвращает список общих друзей двоих пользователей")
    void shouldReturnCommonFriendsOfUsersById() throws Exception {
        final long firstUserId = 3;
        final long secondUserId = 4;
        final List<User> expectedCommonFriends = List.of(firstUser);
        createTestUsers();

        assertEquals(expectedCommonFriends, userService.findCommonFriends(firstUserId, secondUserId), "Список " +
                " общих друзей не совпадает с ожидаемым");

    }
}
