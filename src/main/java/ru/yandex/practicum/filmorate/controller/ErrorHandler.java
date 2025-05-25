package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlerNotFound(final ModelNotFoundException e) {
        log.error("Не найден элемент: {}", e.getMessage());
        return Map.of("not found", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerValidation(final ValidationException e) {
        log.error("Параметр не прошел валидацию: {}", e.getMessage());
        return Map.of("bad request", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handlerRuntime(final RuntimeException e) {
        log.error("Возникла непредвиденная ошибка: {}", e.getMessage());
        return Map.of("internal server error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerDuplicate(final DuplicateException e) {
        log.error("Дублирование параметра или сущности: {}", e.getMessage());
        return Map.of("internal server error", e.getMessage());
    }
}