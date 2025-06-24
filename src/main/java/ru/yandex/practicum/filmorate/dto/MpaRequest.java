package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MpaRequest {

    @NotBlank(message = "Название MPA-рейтинга не может быть пустым.")
    private String name;
}