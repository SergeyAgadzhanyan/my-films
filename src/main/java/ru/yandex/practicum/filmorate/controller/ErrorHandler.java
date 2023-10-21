package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handelValidationException(final ValidationException e) {
        log.error("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return new ErrorResponse("Ошибка валидации: ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handelMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return new ErrorResponse("Ошибка валидации: ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handelNullPointerException(final NullPointerException e) {
        log.error("Получен статус 404 Not found {}", e.getMessage(), e);
        return new ErrorResponse("Искомый объект не найден", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handelEntityNotFoundException(final EntityNotFoundException e) {
        log.error("Получен статус 404 Not found {}", e.getMessage(), e);
        return new ErrorResponse("Искомый объект не найден", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handelRuntimeException(final Exception e) {
        log.error("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return new ErrorResponse("Возникло исключение", e.getMessage());
    }
}
