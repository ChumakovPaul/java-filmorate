package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    User create(User user);

    User update(User newUser);

    void deleteUser(Long id);

    Collection<User> findAll();

    Optional<User> getUserById(Long id);

    User setNewFriendship(Long l1, Long l2);

    List<User> deleteFriend(long l1, long l2);

    List<User> getFriends(Long userId);

    List<User> getFriendReciprocity(long id1, long id2);

    Optional<User> loginValidation(String login);

    Optional<User> emailValidation(String login);

    Set<Long> getFilmLikes(Long id);
}