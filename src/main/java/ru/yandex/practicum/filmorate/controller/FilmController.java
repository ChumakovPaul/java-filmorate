package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @PostMapping
    public FilmDto create(@Valid @RequestBody FilmRequest request) {
        log.info("\nСоздание фильма {}", request);
        return filmService.create(request);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody FilmRequest newFilm) {
        log.info("\nОбновление фильма {}", newFilm);
        return filmService.update(newFilm);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("\nВывод всех фильмов");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public FilmDto getFilmByID(@PathVariable long id) {
        log.info("\nВывод фильма id={}",id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public FilmDto addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("\nПользователь id={} поставил лайк фильму id={}",userId,id);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("\nПользователь id={} удалил лайк фильму id={}",userId,id);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("\nВывод {} самых популярных фильмов",count);
        return filmService.getPopular(count);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable long id) {
        log.info("\nУдаление фильма id={}",id);
        filmService.deleteFilm(id);
    }
}