package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film update(Film newFilm);

    Film create(FilmRequest request);

    Optional<Film> getFilmByID(Long id);

    void deleteFilm(Long id);

    Optional<Film> getFilmWithLike(Long filmId, Long userId);

    Optional<Film> addLike(Long filmId, Long userId);

    Film deleteLike(Long filmId, Long userId);
}