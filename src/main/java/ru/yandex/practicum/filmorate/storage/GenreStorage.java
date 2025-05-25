package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface GenreStorage {

    List<FilmGenre> getGenresOfFilm(Long id);

    void addFilmGenres(Long id, List<Long> values);

    void deleteFilmGenres(Long id);
}