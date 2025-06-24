package ru.yandex.practicum.filmorate.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.GenreRequest;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {

    @Autowired
    private final GenreDbStorage genreDbStorage;

    public GenreDto create(GenreRequest request) {
        if (genreDbStorage.findByName(request.getName()).isPresent()) {
            log.warn("\nЖанр уже существует {}", request);
            throw new DuplicateException("Жанр " + request.getName() + " уже существует.");
        }
        Genre genre = GenreMapper.mapToGenre(request);
        return GenreMapper.mapToGenreDto(genreDbStorage.create(genre));
    }

    public GenreDto update(long id, GenreRequest request) {
        genreDbStorage.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Жанр id = " + id + " не найден."));
        Genre genre = genreDbStorage.findByName(request.getName()).orElse(null);
        if ((genre != null) && (genre.getId() != id)) {
            throw new DuplicateException("Имя " + request.getName() + " уже используется.");
        }
        genre = GenreMapper.mapToGenre(request);
        Genre newGenre = new Genre(id, genre.getName());
        return GenreMapper.mapToGenreDto(genreDbStorage.update(newGenre));
    }

    public GenreDto delete(long id) {
        Genre genre = genreDbStorage.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Жанр id = " + id + " не найден."));
        return GenreMapper.mapToGenreDto(genreDbStorage.delete(genre));
    }

    public List<GenreDto> getAll() {
        return genreDbStorage.getAll().stream().map(GenreMapper::mapToGenreDto).toList();
    }

    public GenreDto getGenreById(long id) {
        return GenreMapper.mapToGenreDto(genreDbStorage.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Жанр id = " + id + " не найден.")));
    }
}