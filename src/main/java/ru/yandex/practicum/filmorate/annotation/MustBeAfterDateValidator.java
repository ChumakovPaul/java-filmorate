package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class MustBeAfterDateValidator  implements ConstraintValidator<MustBeAfterDate, LocalDate> {
    private LocalDate targetDate;

    @Override
    public void initialize(MustBeAfterDate constraintAnnotation) {
        targetDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isBefore(targetDate);
    }
}
