package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Запрос на получение всех фильмов");
        return films.values();
    }

    @Override
    public Film update(Film newFilm) {
        log.info("Обновляем фильм: {}", newFilm);
        if (films.containsKey(newFilm.getId())) {
            if (newFilm.getLikes() == null) {
                newFilm.setLikes(new HashSet<Long>());
            }
            films.put(newFilm.getId(), newFilm);
            log.info("Фильм обновлен: {}", newFilm);
            return newFilm;
        }
        log.error("Фильм с id = {} не найден", newFilm.getId());
        throw new ModelNotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @Override
    public Film create(Film film) {
        log.info("Добавляем фильм: {}", film);
        film.setLikes(new HashSet<Long>());
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @Override
    public Film getFilmByID(Long id) {
        log.info("Запрос на получение фильма по id {}", id);
        if (id <= 0) {
            throw new ModelNotFoundException("Id фильма должен быть положительным");
        }
        if (films.get(id) == null) {
            throw new ModelNotFoundException("Фильм не найден");
        }
        return films.get(id);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}