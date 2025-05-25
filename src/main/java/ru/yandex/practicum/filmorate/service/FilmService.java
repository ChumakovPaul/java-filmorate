package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.MpaDbStorage;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreDbStorage genreStorage;
    private final FilmGenreDbStorage filmGenreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("mpaDbStorage") MpaDbStorage mpaDbStorage,
                       GenreDbStorage genreStorage, FilmGenreDbStorage filmGenreDbStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.filmGenreDbStorage = filmGenreDbStorage;
    }

    public FilmDto create(FilmRequest request) {
        Mpa mpa = mpaDbStorage.findById(request.getMpa().getId())
                .orElseThrow(() -> new ModelNotFoundException("Указан несуществующий рейтинг МПА"));

        Set<Genre> genreSet = new HashSet<>();
        if (request.getGenres() != null) {
            for (Genre g : request.getGenres()) {
                genreSet.add(genreStorage.findById(g.getId())
                        .orElseThrow(() -> new ModelNotFoundException("Указан несуществующий жанр")));
            }
        }
        Film film = filmStorage.create(request);
        filmGenreDbStorage.addFilmGenres(film.getId(), genreSet.stream().map(Genre::getId).toList());
        List<Genre> genres = genreStorage.findByFilmId(film.getId());
        return FilmMapper.mapToFilmDtoWithGenre(film, mpa, genres);
    }

    public FilmDto update(FilmRequest request) {
        if (request.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        Long id = request.getId();
        filmStorage.getFilmByID(id).orElseThrow(() -> new ModelNotFoundException("Film " + request + " not found"));
        Mpa mpa = mpaDbStorage.findById(request.getMpa().getId()).orElseThrow(()
                -> new ModelNotFoundException("Указан несуществующий рейтинг МПА"));
        Film film = FilmMapper.mapToFilm(request);
        film.setId(id);
        Set<Genre> genreSet = new HashSet<>();
        if (request.getGenres() != null) {
            filmGenreDbStorage.deleteFilmGenres(id);
            genreSet.addAll(request.getGenres());
            filmGenreDbStorage.addFilmGenres(film.getId(), genreSet.stream().map(Genre::getId).toList());
        }
        List<Genre> genres = genreStorage.findByFilmId(film.getId());
        return ru.yandex.practicum.filmorate.mappers.FilmMapper.mapToFilmDtoWithGenre(filmStorage.update(film), mpa, genres);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public FilmDto getFilmById(Long id) {
        Film film = filmStorage.getFilmByID(id).orElseThrow(() -> new ModelNotFoundException("Film id " + id + " not found"));
        log.warn("Найден фильм {}", film);
        Mpa mpa = mpaDbStorage.findById(film.getMpa()).orElseThrow(() -> new ModelNotFoundException("Не удалось прочитать МПА"));
        List<Genre> genres = genreStorage.findByFilmId(film.getId());
        return ru.yandex.practicum.filmorate.mappers.FilmMapper.mapToFilmDtoWithGenre(film, mpa, genres);
    }

    public FilmDto addLike(Long id, Long userId) {
        filmStorage.getFilmByID(id).orElseThrow(() -> new ModelNotFoundException("Film id " + id + " not found"));
        userStorage.getUserById(userId).orElseThrow(() -> new ModelNotFoundException("User id = " + userId + " not exist"));
        if (filmStorage.getFilmWithLike(id, userId).isPresent()) {
            log.warn("\nЛайк фильму id={} от пользователя Id={} уже существует", id, userId);
            throw new DuplicateException("Лайк фильму id=" + id + " от пользователя Id=" + userId + " уже существует");
        }
        Film film = filmStorage.addLike(id, userId).get();
        Mpa mpa = mpaDbStorage.findById(film.getMpa()).orElseThrow(() -> new InternalServerException("Не удалось прочитать МПА"));
        Set<Long> likes = userStorage.getFilmLikes(id);
        return FilmMapper.mapToFilmDto(film, mpa, likes);
    }

    public void deleteLike(Long id, Long userId) {
        if (!filmStorage.getFilmWithLike(id, userId).isPresent()) {
            throw new ModelNotFoundException("Пользователь с id = " + userId + " не найден среди лайкнувших этот фильм");
        }
        log.info("Пользователь id={} убрал лайк фильму id={}", userId, id);
        filmStorage.deleteLike(id, userId);
    }

    public Collection<FilmDto> getPopular(int count) {
        log.info("Выводим {} самых популярных фильмов", count);
        return filmStorage.getAllFilms()
                .stream().peek(film -> film.setLikes(userStorage.getFilmLikes(film.getId())))
                .sorted((i, j) -> j.getLikes().size() - i.getLikes().size())
                .limit(count).map(film ->
                        FilmMapper.mapToFilmDto(film, mpaDbStorage.findById(film.getMpa()).get(), film.getLikes()))
                .toList();
    }

    public void deleteFilm(long id) {
        log.info("Удаляем фильм id={}", id);
        filmStorage.deleteFilm(id);
    }
}