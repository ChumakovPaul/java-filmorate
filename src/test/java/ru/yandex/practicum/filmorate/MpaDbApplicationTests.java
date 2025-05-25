package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.MpaDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({
        MpaDbStorage.class,
        MpaRowMapper.class,
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbApplicationTests {

    private final MpaDbStorage mpaStorage;

    @Test
    public void findAllMpaTest() {
        Collection<Mpa> mpaRatings = mpaStorage.getAll();
        assertThat(mpaRatings).isNotNull();
        assertThat(mpaRatings.size()).isGreaterThanOrEqualTo(0);
    }
}