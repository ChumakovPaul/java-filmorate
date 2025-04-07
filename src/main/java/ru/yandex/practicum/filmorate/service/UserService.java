package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserByID(id);
    }

    public void addFriend(Long id, Long friendId) {
        log.info("Добавляются в друзья пользователи id={} и id={} ", id, friendId);
        userStorage.getUserByID(id).getFriends().add(friendId);
        userStorage.getUserByID(friendId).getFriends().add(id);
    }

    public Set<User> getFriends(Long id) {
        log.info("Получаем список друзей пользователя id={}", id);
        Set<User> userFriends = new HashSet<>();
        for (Long friendId : userStorage.getUserByID(id).getFriends()) {
            userFriends.add(userStorage.getUserByID(friendId));
        }
        return userFriends;
    }

    public void deleteFriend(Long id, Long friendId) {
        log.info("Удаление из друзей пользователй id={} и id={}", id, friendId);
        userStorage.getUserByID(id).getFriends().remove(friendId);
        userStorage.getUserByID(friendId).getFriends().remove(id);
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        log.info("Получаем список общих друзей пользователей id={} и id={}", id, otherId);
        return getFriends(id).stream()
                .filter(getFriends(otherId)::contains)
                .collect(Collectors.toSet());
    }
}
