package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage extends BaseRepository<User> implements UserStorage {

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?" +
            " WHERE id = ?";

    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String GET_FRIENDS_QUERY = "SELECT u.* " +
            "FROM users u " +
            "JOIN friends f ON u.id = f.friend_id " +
            "WHERE f.user_id = ? ;";

    private static final String GET_FILM_LIKES_QUERY =
            "SELECT u.* FROM users u JOIN likes l ON u.id = l.user_id  WHERE l.film_id = ?;";

    private static final String GET_COMMON_FRIENDS_QUERY = "SELECT u.* FROM users u " +
            "WHERE u.id IN " +
            "(SELECT friend_id FROM friends WHERE user_id IN (?, ?)) " +
            "AND u.id NOT IN (?, ?);";

    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM FRIENDS " +
            "WHERE user_id = ? " +
            "AND friend_id = ?;";

    private static final String INSERT_FRIENDSHIP_QUERY =
            "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
    private static final String FIND_BY_LOGIN_QUERY = "SELECT * FROM users WHERE login = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User create(User user) {
        insert(
                CREATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );

        return emailValidation(user.getEmail())
                .orElseThrow(() -> new InternalServerException("Ошибка при чтении данных пользователя"));
    }

    public void deleteUser(Long id) {
        delete(DELETE_USER_QUERY, id);
    }

    @Override
    public User update(User user) {
        update(
                UPDATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public Collection<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return findMany(GET_FRIENDS_QUERY, userId);
    }

    @Override
    public User setNewFriendship(Long userId, Long friendId) {
        insert(INSERT_FRIENDSHIP_QUERY, userId, friendId);
        return getUserById(friendId)
                .orElseThrow(() -> new ModelNotFoundException("Ошибка при чтении данных пользователя id = " + friendId));
    }

    @Override
    public List<User> deleteFriend(long userId, long friendId) {
        delete(DELETE_FRIENDSHIP_QUERY, userId, friendId);
        return getFriends(userId);
    }

    @Override
    public List<User> getFriendReciprocity(long userId, long friendId) {
        return findMany(GET_COMMON_FRIENDS_QUERY, userId, friendId, userId, friendId);
    }

    @Override
    public Optional<User> emailValidation(String email) {
        return findOne(FIND_BY_EMAIL_QUERY, email);
    }

    @Override
    public Optional<User> loginValidation(String login) {
        return findOne(FIND_BY_LOGIN_QUERY, login);
    }

    public Set<Long> getFilmLikes(Long id) {
        List<User> users = findMany(GET_FILM_LIKES_QUERY, id);
        System.out.println();
        return users.stream().map(User::getId).collect(Collectors.toSet());
    }
}