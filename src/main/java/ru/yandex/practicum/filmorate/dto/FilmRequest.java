package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotation.MustBeAfterDate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmRequest {

    @Positive(message = "Id фильма должен быть положительным числом")
    private Long id;

    @NotNull(message = "Название фильма не может быть пустым.")
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(min = 1, max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    @MustBeAfterDate(value = "1895-12-28", message = "Дата релиза — не раньше 28 декабря 1895 года.")
    private LocalDate releaseDate;

    @Min(value = 1, message = "Продолжительность фильма должна быть положительным числом.")
    private int duration; //пока предполагаем продолжительность в минутах

    @NotNull(message = "Рейтинг должен быть указан")
    private Mpa mpa;

    private List<Genre> genres;

    public FilmRequest(String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
}