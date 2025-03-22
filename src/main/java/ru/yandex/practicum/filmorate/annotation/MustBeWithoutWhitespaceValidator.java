package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MustBeWithoutWhitespaceValidator implements ConstraintValidator<MustBeWithoutWhitespace, String> {
    @Override
    public void initialize(MustBeWithoutWhitespace constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        return s.indexOf(" ") == -1;
    }
}
