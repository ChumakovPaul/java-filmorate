package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotation.MustBeWithoutWhitespace;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @Positive(message = "Id пользователя должен быть положительным числом")
    private Long id;

    //Поскольку контроллер теперь принимает userRequest, аннотации перенес в класс UserRequest
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;

    @MustBeWithoutWhitespace(message = "Логин не может быть с пробелами")
    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public UserRequest(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}