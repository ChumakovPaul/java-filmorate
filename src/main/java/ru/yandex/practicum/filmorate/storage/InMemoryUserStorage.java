package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        log.info("Cоздаем нового пользователя: {}", user);
        userNameValidation(user);
        user.setId(getNextId());
        user.setFriends(new HashSet<Long>());
        users.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @Override
    public User update(User newUser) {
        log.info("Обновляем пользователя: {}", newUser);
        if (newUser.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            userNameValidation(newUser);
            users.put(newUser.getId(), newUser);
            log.info("Пользователь обновлен: {}", newUser);
            return newUser;
        }
        log.error("Пользователь с id = {} не найден", newUser.getId());
        throw new ModelNotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Запрос всех пользователей");
        return users.values();
    }

    @Override
    public User getUserByID(Long id) {
        log.info("Запрос информации о пользователе id={}", id);
        if (id <= 0) {
            throw new ValidationException("Id пользователя должен быть положительным");
        }
        if (users.get(id) == null) {
            throw new ModelNotFoundException("Пользователь c Id = " + id + " не найден");
        }
        return users.get(id);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void userNameValidation(User user) {
        log.info("Валидация имени нового пользователя");
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}