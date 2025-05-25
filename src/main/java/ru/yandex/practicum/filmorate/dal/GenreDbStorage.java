package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Component("genreDbStorage")
public class GenreDbStorage extends BaseRepository<Genre> {
    private static final String CREATE_GENRE_QUERY = "INSERT INTO genre(name) VALUES (?)";
    private static final String UPDATE_GENRE_QUERY = "UPDATE genre SET name = ? WHERE genre_id = ?";
    private static final String DELETE_GENRE_QUERY = "DELETE FROM genre WHERE genre_id = ?";
    private static final String GET_ALL_GENRE = "SELECT * FROM genre";
    private static final String GET_FILM_GENRES_QUERY =
            "SELECT g.genre_id, g.name FROM film_genres fg LEFT JOIN genre g " +
                    "ON fg.genre_id = g.genre_id WHERE fg.film_id = ? ORDER BY g.genre_id";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM genre WHERE name = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Genre create(Genre genre) {
        insert(CREATE_GENRE_QUERY, genre.getName());
        return findByName(genre.getName())
                .orElseThrow(() -> new InternalServerException("Ошибка при чтении данных genre"));
    }

    public Genre update(Genre genre) {
        update(UPDATE_GENRE_QUERY, genre.getName(), genre.getId());
        return genre;
    }

    public List<Genre> findByFilmId(Long id) {
        return findMany(GET_FILM_GENRES_QUERY, id);
    }

    public Genre delete(Genre genre) {
        if (delete(DELETE_GENRE_QUERY, genre.getId()))
            return genre;
        else
            throw new InternalServerException("Не удалось удалить " + genre);
    }

    public List<Genre> getAll() {
        return findMany(GET_ALL_GENRE);
    }

    public Optional<Genre> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Optional<Genre> findByName(String name) {
        return findOne(FIND_BY_NAME_QUERY, name);
    }
}