package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = MustBeWithoutWhitespaceValidator.class)

public @interface MustBeWithoutWhitespace {
    String message();
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
