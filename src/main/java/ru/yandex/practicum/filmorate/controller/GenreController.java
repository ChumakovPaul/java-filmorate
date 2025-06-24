package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.GenreRequest;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;


    @PostMapping
    public GenreDto create(@Valid @RequestBody GenreRequest request) {
        log.info("\nСоздание жанра {}", request);
        return genreService.create(request);
    }

    @PutMapping("/{id}")
    public GenreDto update(@PathVariable long id,
                           @Valid @RequestBody GenreRequest request) {
        log.info("\nОбновление жанра{}", id);
        return genreService.update(id, request);
    }


    @DeleteMapping("/{id}")
    public GenreDto deleteGenre(@PathVariable long id) {
        log.info("\nУдаление жанра {}", id);
        return genreService.delete(id);
    }

    @GetMapping
    public List<GenreDto> getAll() {
        log.info("\nВывод всех жанров");
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public GenreDto getGenre(@PathVariable long id) {
        log.info("\nВывод жанра id={}", id);
        return genreService.getGenreById(id);
    }
}