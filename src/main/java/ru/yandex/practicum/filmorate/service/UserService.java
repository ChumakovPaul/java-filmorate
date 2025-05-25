package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.UserRequest;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
//@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto create(UserRequest request) {
        log.warn("Создаем пользователя {}", request);
        userNameValidation(request);
        if (userStorage.loginValidation(request.getLogin()).isPresent()) {
            log.warn("\nНе создан {}", request);
            throw new DuplicateException("Логин " + request.getLogin() + " уже используется.");
        }
        if (userStorage.emailValidation(request.getEmail()).isPresent()) {
            log.warn("\nНе создан {}", request);
            throw new DuplicateException("Электронная почта " + request.getEmail() + " уже используется.");
        }
        User user = UserMapper.mapToUser(request);
        user.setFriends(new TreeSet<Long>());
        return UserMapper.mapToUserDto(userStorage.create(user));
    }

    public UserDto update(UserRequest request) {
        if (request.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        User testUser = userStorage.getUserById(request.getId())
                .orElseThrow(() -> new ModelNotFoundException("Пользователь id = " + request.getId() + " не найден "));
        if (userStorage.loginValidation(request.getLogin()).isPresent()) {
            log.warn("\nНе создан {}", request);
            throw new DuplicateException("Логин " + request.getLogin() + " уже используется.");
        }
        if (userStorage.emailValidation(request.getEmail()).isPresent()) {
            log.warn("\nНе создан {}", request);
            throw new DuplicateException("Электронная почта " + request.getEmail() + " уже используется.");
        }
        userNameValidation(request);
        User user = UserMapper.mapToUser(request);
        return UserMapper.mapToUserDto(userStorage.update(user));
    }

    public void deleteUser(Long id) {
        log.info("Удаляется пользователь id={}", id);
        userExistenceValidation(id);
        userStorage.deleteUser(id);
    }

    public Collection<UserDto> getAllUsers() {
        return userStorage.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    public UserDto getUserById(Long id) {
        if (id <= 0) {
            throw new ValidationException("Id пользователя должен быть положительным");
        }
        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new ModelNotFoundException("Пользователь id = " + id + " не найден "));
        return UserMapper.mapToUserDto(user);
    }

    public void addFriend(Long id, Long friendId) {
        log.info("Добавляются в друзья пользователи id={} и id={} ", id, friendId);
        userExistenceValidation(id);
        userExistenceValidation(friendId);
        userStorage.setNewFriendship(id, friendId);
    }

    public Collection<UserDto> getFriends(Long id) {
        log.info("Получаем список друзей пользователя id= " + id);
        userExistenceValidation(id);
        return userStorage.getFriends(id).stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public void deleteFriend(Long id, Long friendId) {
        log.info("Удаление пользователя id={} из списка друзей  пользователя id={}", friendId, id);
        userExistenceValidation(id);
        userExistenceValidation(friendId);
        userStorage.deleteFriend(id, friendId);
    }

    public Collection<UserDto> getFriendReciprocity(Long id, Long otherId) {
        log.info("Получаем список общих друзей пользователей id={} и id={}", id, otherId);
        return userStorage.getFriendReciprocity(id, otherId).stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    private void userNameValidation(UserRequest request) {
        log.info("Валидация имени нового пользователя");
        if (request.getName() == null) {
            request.setName(request.getLogin());
        }
    }

    private void userExistenceValidation(Long id) {
        userStorage.getUserById(id).orElseThrow(()
                -> new ModelNotFoundException("Пользователь с id=" + id + " не найден"));
    }

    public Collection<Long> getFilmLikes(Long id) {
        return userStorage.getFilmLikes(id);
    }
}