package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectFriendIdException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllArgsConstructor
public abstract class UserServiceIntegrationTest {
    protected final UserService userService;

    final User testUser1 = User.builder().id(1)
            .email("galina123@mail.ru")
            .login("GalGadot")
            .name("Galya")
            .birthday(LocalDate.parse("1990-12-21"))
            .build();
    final User testUser2 = User.builder().id(2)
            .email("supermaan@yandex.ru")
            .login("superman12")
            .name("Superman")
            .birthday(LocalDate.parse("2001-05-12"))
            .build();
    final User testUser3 = User.builder().id(3)
            .email("habib@gmail.com")
            .login("AlHabib")
            .name("AlHabib")
            .birthday(LocalDate.parse("1986-10-19"))
            .build();

    final User testUser4 = User.builder().id(4)
            .email("anatoly1965@rambler.ru")
            .login("PrivetFromSSSR")
            .name("Anatoly Petrovich")
            .birthday(LocalDate.parse("1965-05-29"))
            .build();

    @Test
    @DisplayName("Возвращает список всех пользователей")
    public void shouldReturnAllUsers() throws Exception {
        final List<User> expectedUsers = List.of(testUser1, testUser2, testUser3);

        assertEquals(expectedUsers, userService.findAllUsers(), "Список пользователей не совпадает с" +
                " ожидаемым");
    }

    @Test
    @DisplayName("Возвращает пользователя по заданному id, выбрасывает исключение если пользователь с заданным id" +
            " отсутствует")
    void shouldReturnUserById() throws Exception {
        final int id = 3;
        final User expectedUser = testUser3;

        assertEquals(expectedUser, userService.findUser(id), "Пользователь не совпадает с ожидаемым");

        final int wrongId = 34;
        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> userService.findUser(wrongId));

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Добавляет в хранилище нового пользователя. Возвращает добавленного пользователя.")
    void shouldCreateNewUser() throws Exception {
        final User newUser = testUser4;
        final User expectedUser = testUser4;

        assertEquals(expectedUser, userService.createUser(newUser), "Возвращаемый пользователь не" +
                " соответствует ожидаемому");
        assertTrue(userService.findAllUsers().contains(expectedUser), "Пользователь не добавлен в хранилище");
    }

    @Test
    @DisplayName("Обновляет пользователя в хранилище, выбрасывает исключение если пользователь с заданным id" +
            " отсутствует. Возвращает измененного пользователя")
    void shouldUpdateUser() throws Exception {
        final User modifiedUser = User.builder().id(2)
                .email("superman12@yandex.ru")
                .login("superOleg")
                .name("Oleg")
                .birthday(LocalDate.parse("2001-06-10"))
                .build();
        final User expectedUser = modifiedUser;

        assertEquals(expectedUser, userService.updateUser(modifiedUser), "Возвращаемый пользователь не" +
                " соответствует ожидаемому");
        assertEquals(modifiedUser, userService.findUser(modifiedUser.getId()), "Пользователь не обновлен в" +
                " хранилище");

        final User wrongModifiedUser = User.builder().id(25)
                .email("superman12@yandex.ru")
                .login("superOleg")
                .name("Oleg")
                .birthday(LocalDate.parse("2001-06-10"))
                .build();
        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> userService.updateUser(wrongModifiedUser));

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Удаляет пользователя из хранилища по заданному id, выбрасывает исключение если пользователь с" +
            " заданным id отсутствует")
    void shouldDeleteUserById() throws Exception {
        final int id = 2;

        userService.deleteUser(id);

        assertFalse(userService.findAllUsers().contains(testUser2), "Пользователь не удален из хранилища");

        final int wrongId = 453;
        final IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> userService.deleteUser(wrongId));

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Добавляет пользователю друга по заданному id, выбрасывает исключение если пользователь или друг с" +
            " заданным id отсутствует или пользователь добавляет самого себя")
    void shouldAddFriendById() throws Exception {
        final int userId = 2;
        final int friendId = 3;
        final List<User> expectedFriends = List.of(testUser1, testUser3);

        userService.addFriend(userId, friendId);

        assertEquals(expectedFriends, userService.findFriends(userId), "Пользователь не добавлен в друзья");

        final int wrongUserId = 12;
        IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> userService.addFriend(wrongUserId, friendId));

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");

        final int wrongFriendId = 12;
        e = assertThrows(
                IdNotFoundException.class,
                () -> userService.addFriend(userId, wrongFriendId));

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");

        final IncorrectFriendIdException exception = assertThrows(
                IncorrectFriendIdException.class,
                () -> userService.addFriend(userId, userId));

        assertEquals("id пользователей совпадают", exception.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Удаляет друга пользователя по заданному id, выбрасывает исключение если пользователь или друг с" +
            " заданным id отсутствует или пользователь удаляет самого себя")
    void shouldDeleteFriendById() throws Exception {
        final int userId = 2;
        final int friendId = 1;
        final List<User> expectedFriends = new ArrayList<>();

        userService.deleteFriend(userId, friendId);

        assertEquals(expectedFriends, userService.findFriends(userId), "Пользователь не удален из друзей");

        final int wrongUserId = 12;
        IdNotFoundException e = assertThrows(
                IdNotFoundException.class,
                () -> userService.deleteFriend(wrongUserId, friendId));

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");

        final int wrongFriendId = 12;
        e = assertThrows(
                IdNotFoundException.class,
                () -> userService.deleteFriend(userId, wrongFriendId));

        assertEquals("пользователь с заданным id не найден", e.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");

        final IncorrectFriendIdException exception = assertThrows(
                IncorrectFriendIdException.class,
                () -> userService.deleteFriend(userId, userId));

        assertEquals("id пользователей совпадают", exception.getMessage(), "Сообщение об исключении" +
                " не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Возвращает список всех друзей пользователя")
    public void ShouldReturnFriendsOfUser() throws Exception {
        final int userId = 2;
        final List<User> expectedFriends = List.of(testUser1, testUser3);

        userService.addFriend(2, 3);

        assertEquals(expectedFriends, userService.findFriends(userId), "Список друзей не соответствует" +
                " ожидаемому");
    }

    @Test
    @DisplayName("Возвращает список общих друзей двух пользователей")
    public void shouldReturnCommonFriendsOfUsers() throws Exception {
        final int userId = 1;
        final int otherUserId = 3;
        final List<User> expectedFriends = List.of(testUser2);

        userService.addFriend(3, 2);

        assertEquals(expectedFriends, userService.findCommonFriends(userId, otherUserId), "Список друзей не соответствует" +
                " ожидаемому");
    }
}
