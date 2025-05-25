package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserDbStorage.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbApplicationTests {

    private final UserDbStorage userStorage;
    private User user1;

    @BeforeEach
    public void setup() {
        user1 = new User();
        user1.setName("User One");
        user1.setEmail("user1@mail.com");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1 = userStorage.create(user1);
    }

    @Test
    public void findUserByIdTest() {
        assertThat(user1.getId()).isNotNull();
        Optional<User> user2 = userStorage.getUserById(user1.getId());

        assertThat(user2)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getName()).isEqualTo("User One");
                    assertThat(user.getEmail()).isEqualTo("user1@mail.com");
                    assertThat(user.getBirthday().isEqual(user1.getBirthday()));
                });
    }

    @Test
    public void updateUserTest() {
        assertThat(user1.getId()).isNotNull();
        user1.setName("Обновленное имя");
        User user2 = userStorage.update(user1);
        assertThat(user2.getName()).isEqualTo("Обновленное имя");
        assertThat(user2.getLogin()).isEqualTo("user1");
    }

    @Test
    public void deleteUserTest() {
        assertThat(user1.getId()).isNotNull();
        userStorage.deleteUser(user1.getId());
        assertThat(userStorage.getUserById(user1.getId())).isEmpty();
    }

    @Test
    public void getUserFriendsTest() {
        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("user2@mail.com");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.of(1991, 2, 2));
        user2 = userStorage.create(user2);
        userStorage.setNewFriendship(user1.getId(), user2.getId());
        assertThat(userStorage.getFriends(user1.getId()).size()).isEqualTo(1);
        assertThat(userStorage.getFriends(user1.getId()).getFirst()).isEqualTo(user2);
        assertThat(userStorage.getFriends(user2.getId()).size()).isEqualTo(0);
    }

    @Test
    public void deleteFriendTest() {
        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("user2@mail.com");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.of(1991, 2, 2));
        user2 = userStorage.create(user2);
        User user3 = new User();
        user3.setName("User Three");
        user3.setEmail("user3@mail.com");
        user3.setLogin("user3");
        user3.setBirthday(LocalDate.of(1992, 3, 3));
        user3 = userStorage.create(user3);
        userStorage.setNewFriendship(user1.getId(), user2.getId());
        userStorage.setNewFriendship(user1.getId(), user3.getId());
        assertThat(userStorage.getFriends(user1.getId()).size()).isEqualTo(2);
        assertThat(userStorage.getFriends(user1.getId()).getFirst()).isEqualTo(user2);
        userStorage.deleteFriend(user1.getId(), user2.getId());
        assertThat(userStorage.getFriends(user1.getId()).size()).isEqualTo(1);
        assertThat(userStorage.getFriends(user1.getId()).getFirst()).isEqualTo(user3);
    }

    @Test
    public void getFriendReciprocityTest() {
        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("user2@mail.com");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.of(1991, 2, 2));
        user2 = userStorage.create(user2);
        User user3 = new User();
        user3.setName("User Three");
        user3.setEmail("user3@mail.com");
        user3.setLogin("user3");
        user3.setBirthday(LocalDate.of(1992, 3, 3));
        user3 = userStorage.create(user3);
        userStorage.setNewFriendship(user1.getId(), user3.getId());
        userStorage.setNewFriendship(user2.getId(), user3.getId());
        assertThat(userStorage.getFriendReciprocity(user1.getId(), user2.getId()).getFirst()).isEqualTo(user3);
    }
}