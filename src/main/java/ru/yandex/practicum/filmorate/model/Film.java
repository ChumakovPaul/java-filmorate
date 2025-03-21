package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class Film {
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(min = 1,max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    private LocalDate releaseDate;

    @Min(value = 1, message = "Продолжительность фильма должна быть положительным числом.")
    private int duration;
}
