package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MustBeWithoutWhitespace;

import java.time.LocalDate;

@Builder
@Data
public class User {

    private Long id;

    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;

    @MustBeWithoutWhitespace(message = "Логин не может быть с пробелами")
    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
