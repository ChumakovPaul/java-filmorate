package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({
        GenreDbStorage.class,
        GenreRowMapper.class,
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbApplicationTests {

    private final GenreDbStorage genreStorage;

    @Test
    public void findAllGenresTest() {
        Collection<Genre> genres = genreStorage.getAll();
        assertThat(genres).isNotNull();
        assertThat(genres.size()).isEqualTo(6);
    }
}