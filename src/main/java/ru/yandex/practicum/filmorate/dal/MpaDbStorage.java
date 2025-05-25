package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Component("ratingDbStorage")
public class MpaDbStorage extends BaseRepository<Mpa> implements MpaStorage {

    private static final String CREATE_MPA_QUERY = "INSERT INTO mpa(name) VALUES (?)";
    private static final String MODIFY_MPA_QUERY = "UPDATE mpa SET name = ? WHERE mpa_id = ?";
        private static final String DELETE_RATING_QUERY = "DELETE FROM mpa WHERE mpa_id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM mpa";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE mpa_id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM mpa WHERE name = ?";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Mpa create(Mpa mpa) {
        insert(CREATE_MPA_QUERY, mpa.getName());
        return findByName(mpa.getName())
                .orElseThrow(() -> new InternalServerException("Ошибка при чтении данных rating"));
    }

    @Override
    public Mpa update(Mpa mpa) {
        update(MODIFY_MPA_QUERY, mpa.getName(), mpa.getId());
        return mpa;
    }

    @Override
    public List<Mpa> getAll() {
        return findMany(GET_ALL_QUERY);
    }

    @Override
    public Optional<Mpa> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public void deleteMpa(Long id) {
        delete(DELETE_RATING_QUERY, id);
    }

    @Override
    public Optional<Mpa> findByName(String name) {
        return findOne(FIND_BY_NAME_QUERY, name);
    }
}