package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MustBeAfterDate;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
public class Film {
    private Long id;

    @NotNull(message = "Название фильма не может быть пустым.")
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(min = 1, max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    @MustBeAfterDate(value = "1895-12-28", message = "Дата релиза — не раньше 28 декабря 1895 года.")
    private LocalDate releaseDate;

    @Min(value = 1, message = "Продолжительность фильма должна быть положительным числом.")
    private int duration;

    private Set<Long> likes;
}