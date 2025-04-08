package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        return filmStorage.update(newFilm);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        if (id <= 0) {
            throw new ValidationException("Id фильма должен быть положительным");
        }
        log.info("Запрос информации о фильме id={}", id);
        return filmStorage.getFilmByID(id);
    }

    public void addLike(Long id, Long userId) {
        if (userStorage.getUserByID(userId) == null) {
            throw new ModelNotFoundException("Пользователь с id = " + userId + " не найден");
        }
        log.info("Пользователь id={} поставил лайк фильму id={}", userId, id);
        filmStorage.getFilmByID(id).getLikes().add(userId);
    }

    public void deleteLike(Long id, Long userId) {
        if (!filmStorage.getFilmByID(id).getLikes().contains(userId)) {
            throw new ModelNotFoundException("Пользователь с id = " + userId + " не найден среди лайкнувших этот фильм");
        }
        log.info("Пользователь id={} убрал лайк фильму id={}", userId, id);
        filmStorage.getFilmByID(id).getLikes().remove(userId);
    }

    public Collection<Film> getPopular(int count) {
        log.info("Выводим {} самых популярных фильмов", count);
        return filmStorage.getAllFilms()
                .stream()
                .sorted((i, j) -> j.getLikes().size() - i.getLikes().size())
                .limit(count)
                .toList();
    }
}