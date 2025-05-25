package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.UserRequest;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserRequest request) {
        log.info("\nСоздание пользователя {}", request);
        return userService.create(request);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UserRequest request) {
        log.info("\nОбновление пользователя {}", request);
        return userService.update(request);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("\nВывод всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("\nВывод пользователя id={}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("\nВользователь id={} добавил в друзья пользователя id={}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> getFriends(@PathVariable Long id) {
        log.info("\nВыводв всех друзей пользователя id={}", id);
        return userService.getFriends(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("\nУдаление пользователя id={}", id);
        userService.deleteUser(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("\nУдаление пользователя id={} из списка друзей пользователя id={}", friendId, id);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDto> getFriendReciprocity(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("\nВывод общих друзей пользователей id={} и id={}", id);
        return userService.getFriendReciprocity(id, otherId);
    }
}