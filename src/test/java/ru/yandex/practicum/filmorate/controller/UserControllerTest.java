package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {
    UserStorage userStorage;
    UserService userService;
    UserController userController;
    User user1;
    User user2;

    @BeforeEach
    public void beforeEach() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    void shouldCreateUserTest() {
        user1 = User.builder()
                .name("User name")
                .email("test@email.ru")
                .birthday(LocalDate.parse("2002-04-04"))
                .login("login")
                .build();
        userController.create(user1);
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void shouldBeErrorWithWrongEmailTest() {
        user1 = User.builder()
                .name("User name")
                .email("testemail.ru")
                .birthday(LocalDate.parse("2002-04-04"))
                .login("login")
                .build();
        assertEquals(0, userController.getAllUsers().size());
        user2 = User.builder()
                .name("User name 2")
                .birthday(LocalDate.parse("2002-04-04"))
                .login("login")
                .build();
        assertEquals(0, userController.getAllUsers().size());
    }

    @Test
    void shouldBeErrorWithWrongLoginTest() {
        user1 = User.builder()
                .name("User name")
                .email("test@email.ru")
                .birthday(LocalDate.parse("2002-04-04"))
                .login("log in")
                .build();
        assertEquals(0, userController.getAllUsers().size());
        user2 = User.builder()
                .name("User name 2")
                .email("test@email.ru")
                .birthday(LocalDate.parse("2002-04-04"))
                .build();
        assertEquals(0, userController.getAllUsers().size());
    }

    @Test
    void shouldCreateUserWihtoutNameTest() {
        user1 = User.builder()
                .email("test@email.ru")
                .birthday(LocalDate.parse("2002-04-04"))
                .login("login")
                .build();
        userController.create(user1);
        assertEquals("login", userController.getAllUsers().stream().toList().get(0).getName());
    }

    @Test
    void shouldBeErrorWithBirthdayInFutureTest() {
        user1 = User.builder()
                .name("User name")
                .email("test@email.ru")
                .birthday(LocalDate.parse("2032-04-04"))
                .login("log in")
                .build();
        assertEquals(0, userController.getAllUsers().size());
    }

    @Test
    void shouldBeErrorWithUpdateUserWithoutIdTest() {
        user1 = User.builder()
                .name("User name 1")
                .email("test@email.ru")
                .birthday(LocalDate.parse("2002-04-04"))
                .login("login")
                .build();
        userController.create(user1);
        user2 = User.builder()
                .name("User name 2")
                .email("test@email.ru")
                .birthday(LocalDate.parse("2002-04-04"))
                .build();
        userController.create(user2);
        assertEquals("User name 1", userController.getAllUsers().stream().toList().get(0).getName());
    }

    @Test
    void shouldBeUpdateUserTest() {
        user1 = User.builder()
                .name("User name")
                .email("test@email.ru")
                .birthday(LocalDate.parse("2002-04-04"))
                .login("login")
                .build();
        userController.create(user1);
        user2 = User.builder()
                .id(1L)
                .name("User name 2")
                .email("test@email.ru")
                .birthday(LocalDate.parse("2002-04-04"))
                .login("login")
                .build();
        userController.update(user2);
        assertEquals("User name 2", userController.getAllUsers().stream().toList().get(0).getName());
    }

    @Test
    void shouldReturnAllUsersTest() {
        user1 = User.builder()
                .name("User name")
                .email("test@email.ru")
                .birthday(LocalDate.parse("2002-04-04"))
                .login("login")
                .build();
        userController.create(user1);
        user2 = User.builder()
                .name("User name 2")
                .email("test@email.ru")
                .birthday(LocalDate.parse("2002-04-04"))
                .login("login")
                .build();
        userController.create(user2);
        assertEquals(2, userController.getAllUsers().size());
    }
}