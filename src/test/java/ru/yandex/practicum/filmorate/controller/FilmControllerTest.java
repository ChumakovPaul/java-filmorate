package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController filmController;
    Film film1;
    Film film2;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void shouldCreateFilmTest() {
        film1 = Film.builder()
                .name("Film name 1")
                .description("Film 1 description")
                .duration(150)
                .releaseDate(LocalDate.parse("2002-03-25"))
                .build();
        filmController.create(film1);
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    void shouldBeErrorWithoutFilmNameTest() {
        film1 = Film.builder()
                .description("Film 1 description")
                .duration(150)
                .releaseDate(LocalDate.parse("2002-03-25"))
                .build();
        assertEquals(0, filmController.getAllFilms().size());
    }

    @Test
    void shouldBeErrorWithLongDescriptionTest() {
        String filmDescription = "Long Film 1 description".repeat(200);
        film1 = Film.builder()
                .name("Film name 1")
                .description(filmDescription)
                .duration(150)
                .releaseDate(LocalDate.parse("2002-03-25"))
                .build();
        assertEquals(0, filmController.getAllFilms().size());
    }

    @Test
    void shouldBeErrorWithEarlyReleaseDateTest() {
        film1 = Film.builder()
                .name("Film name 1")
                .description("Film 1 description")
                .duration(150)
                .releaseDate(LocalDate.parse("1800-03-25"))
                .build();
        assertEquals(0, filmController.getAllFilms().size());
    }

    @Test
    void shouldBeErrorWithNegativeDurationTest() {
        film1 = Film.builder()
                .name("Film name 1")
                .description("Film 1 description")
                .duration(-150)
                .releaseDate(LocalDate.parse("2003-03-25"))
                .build();
        assertEquals(0, filmController.getAllFilms().size());
    }

    @Test
    void shouldBeErrorWithUpdateFilmWihoutIdTest() {
        film1 = Film.builder()
                .name("Film name 1")
                .description("Film 1 description")
                .duration(150)
                .releaseDate(LocalDate.parse("2003-03-25"))
                .build();
        film2 = Film.builder()
                .name("Film name 2")
                .description("Film 2 description")
                .duration(150)
                .releaseDate(LocalDate.parse("2003-03-25"))
                .build();
        filmController.create(film1);
        assertThrows(ValidationException.class, () -> {
            filmController.update(film2);
        });
    }

    @Test
    void shouldUpadteFilmTest() {
        film1 = Film.builder()
                .name("Film name 1")
                .description("Film 1 description")
                .duration(150)
                .releaseDate(LocalDate.parse("2003-03-25"))
                .build();
        film2 = Film.builder()
                .id(1L)
                .name("Film name 2")
                .description("Film 2 description")
                .duration(150)
                .releaseDate(LocalDate.parse("2003-03-25"))
                .build();
        filmController.create(film1);
        filmController.update(film2);
        assertEquals("Film name 2", filmController.getAllFilms().stream().toList().get(0).getName());
    }

    @Test
    void shouldReturnAllFilmsTest() {
        film1 = Film.builder()
                .name("Film name 1")
                .description("Film 1 description")
                .duration(150)
                .releaseDate(LocalDate.parse("2003-03-25"))
                .build();
        film2 = Film.builder()
                .name("Film name 2")
                .description("Film 2 description")
                .duration(150)
                .releaseDate(LocalDate.parse("2003-03-25"))
                .build();
        filmController.create(film1);
        filmController.create(film2);
        assertEquals(2, filmController.getAllFilms().size());
    }
}