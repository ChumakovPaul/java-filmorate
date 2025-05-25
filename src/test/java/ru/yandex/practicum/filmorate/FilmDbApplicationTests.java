package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dal.mappers.*;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@JdbcTest
@AutoConfigureTestDatabase
@Import({UserDbStorage.class,
        UserRowMapper.class,
        FilmDbStorage.class,
        FilmRowMapper.class,
        GenreDbStorage.class,
        GenreRowMapper.class,
        MpaDbStorage.class,
        MpaRowMapper.class,
        FilmGenreDbStorage.class,
        FilmGenreRowMapper.class,
        FilmRequest.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbApplicationTests {

    private final UserDbStorage userStorage;
    private User user1;
    private final FilmDbStorage filmStorage;
    private Film film1;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;
    private final FilmGenreDbStorage filmGenreStorage;
    private FilmRequest filmRequest;

    @BeforeEach
    public void setup() {
        user1 = new User();
        user1.setName("User One");
        user1.setEmail("user1@mail.com");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1 = userStorage.create(user1);
        Mpa mpa = mpaStorage.findById(1L).get();
        filmRequest = new FilmRequest("Film1", "Description1",
                LocalDate.of(2000, 01, 01), 90, mpa);
        film1 = filmStorage.create(filmRequest);
        filmGenreStorage.addFilmGenres(film1.getId(), List.of(1L, 2L));
        filmStorage.addLike(film1.getId(), user1.getId());
    }

    @Test
    public void findFilmByIdTest() {
        assertThat(film1.getId()).isNotNull();
        Optional<Film> film2 = filmStorage.getFilmByID(film1.getId());
        assertThat(film2)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getName()).isEqualTo("Film1");
                    assertThat(film.getDescription()).isEqualTo("Description1");
                });
    }

    @Test
    public void updateFilmTest() {
        film1.setName("Update Description1");
        film1.setMpa(mpaStorage.findById(2L).get().getId());
        Film updatedFilm = filmStorage.update(film1);
        assertThat(updatedFilm.getName()).isEqualTo("Update Description1");
        assertThat(updatedFilm.getMpa()).isEqualTo(mpaStorage.findById(2L).get().getId());
    }

    @Test
    public void deleteFilmTest() {
        assertThat(film1.getId()).isNotNull();
        filmStorage.deleteFilm(film1.getId());
        assertThat(filmStorage.getFilmByID(film1.getId())).isEmpty();
    }

    @Test
    public void addLikeTest() {
        Set<Long> filmLikes = userStorage.getFilmLikes(film1.getId());
        assertThat(filmLikes).isNotEmpty();
    }

    @Test
    public void deleteLikeTest() {
        filmStorage.deleteLike(film1.getId(), user1.getId());
        Set<Long> filmLikes = userStorage.getFilmLikes(film1.getId());
        assertThat(filmLikes).isEmpty();
    }

    @Test
    public void addLikeToNotExistenceFilmTest() {
        assertThatThrownBy(() ->
                filmStorage.addLike(2L, user1.getId())
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void getPopularFilmsTest() {
        List<Film> popularFilms = filmStorage.getPopular(1);
        assertThat(popularFilms).isNotEmpty();
        assertThat(popularFilms.get(0).getId()).isEqualTo(film1.getId());
    }
}