package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {

    private static final String INSERT_FILM_QUERY =
            "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_FILM_QUERY =
            "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?,  mpa_id = ? WHERE film_id = ?";

    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String GET_ALL_FILMS_QUERY = "SELECT * FROM films";
    private static final String INSERT_NEW_LIKE_QUERY = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String GET_FILM_WITH_LIKE_QUERY =
            "SELECT * FROM films WHERE film_id IN (SELECT film_id FROM likes WHERE film_id = ? AND user_id = ?)";

    private static final String GET_POPULAR_FILMS_QUERY =
            " SELECT f.film_id, name, description, duration, release_date, mpa_id, COUNT(l.film_id) as count " +
                    "FROM films f LEFT JOIN likes l ON f.film_id = l.film_id GROUP BY f.film_id " +
                    "ORDER BY count desc, f.name LIMIT ?";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film create(FilmRequest request) {
        Long id = insert(
                INSERT_FILM_QUERY,
                request.getName(),
                request.getDescription(),
                request.getReleaseDate(),
                request.getDuration(),
                request.getMpa().getId()
        );
        Film film = getFilmByID(id)
                .orElseThrow(() -> new InternalServerException("Ошибка при чтении данных фильма"));
        return film;
    }

    @Override
    public Optional<Film> getFilmByID(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Film update(Film film) {
        update(
                UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa(),
                film.getId()
        );
        return film;
    }

    public void deleteFilm(Long id) {
        if (!delete(DELETE_FILM_QUERY, id)) {
            throw new InternalServerException("Не удалось удалить " + id);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        return findMany(GET_ALL_FILMS_QUERY);
    }

    public List<Film> getPopular(long count) {
        return findMany(GET_POPULAR_FILMS_QUERY, count);
    }

    public Optional<Film> getFilmWithLike(Long filmId, Long userId) {
        return findOne(GET_FILM_WITH_LIKE_QUERY, filmId, userId);
    }

    public Optional<Film> addLike(Long filmId, Long userId) {
        insert(INSERT_NEW_LIKE_QUERY, userId, filmId);
        return getFilmByID(filmId);
    }

    public Film deleteLike(Long filmId, Long userId) {
        delete(DELETE_LIKE_QUERY, filmId, userId);
        return getFilmByID(filmId).orElseThrow(() -> new InternalServerException("Ошибка при чтении данных фильма"));
    }
}