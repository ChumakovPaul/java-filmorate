//package ru.yandex.practicum.filmorate.storage;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.util.*;
//
//@Slf4j
//@Component("inMemoryUserStorage")
//public class InMemoryUserStorage implements UserStorage {
//
//    private final Map<Long, User> users = new HashMap<>();
//
//    @Override
//    public User create(User user) {
//        log.info("Cоздаем нового пользователя: {}", user);
//        user.setId(getNextId());
//        user.setFriends(new HashSet<Long>());
//        users.put(user.getId(), user);
//        log.info("Пользователь создан: {}", user);
//        return user;
//    }
//
//    @Override
//    public User update(User newUser) {
//        log.info("Обновляем пользователя: {}", newUser);
//        if (users.containsKey(newUser.getId())) {
//            users.put(newUser.getId(), newUser);
//            log.info("Пользователь обновлен: {}", newUser);
//            return newUser;
//        }
//        log.error("Пользователь с id = {} не найден", newUser.getId());
//        throw new ModelNotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
//    }
//
//    @Override
//    public void deleteUser(Long id) {
//    }
//
//    @Override
//    public Collection<User> findAll() {
//        log.info("Запрос всех пользователей");
//        return users.values();
//    }
//
//    @Override
//    public Optional<User> getUserById(Long id) {
//        return Optional.ofNullable(users.get(id));
//    }
//
//    @Override
//    public User setNewFriendship(Long l1, Long l2) {
//        return null;
//    }
//
//    @Override
//    public List<User> deleteFriend(long l1, long l2) {
//        return List.of();
//    }
//
//    @Override
//    public List<User> getFriends(Long userId) {
//        return List.of();
//    }
//
//    @Override
//    public List<User> getFriendReciprocity(long id1, long id2) {
//        return List.of();
//    }
//
//    @Override
//    public Optional<User> loginValidation(String login) {
//        return users.keySet().stream()
//                .map(users::get)
//                .filter(user -> user.getLogin().equals(login))
//                .findFirst();
//    }
//    private long getNextId() {
//        long currentMaxId = users.keySet()
//                .stream()
//                .mapToLong(id -> id)
//                .max()
//                .orElse(0);
//        return ++currentMaxId;
//    }
//
//    @Override
//    public Optional<User> emailValidation(String login) {
//        return users.keySet().stream()
//                .map(users::get)
//                .filter(user -> user.getLogin().equals(login))
//                .findFirst();
//    }
//
//    @Override
//    public Set<Long> getFilmLikes(Long id) {
//        return Set.of();
//    }
//}