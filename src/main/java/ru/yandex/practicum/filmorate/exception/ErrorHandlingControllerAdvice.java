package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.valid.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.valid.Violation;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            log.error("Ошибка валидации объекта '{}': некорректное значение '{}' поля '{}'; '{}' {}",
                    fieldError.getObjectName(), fieldError.getRejectedValue(), fieldError.getField(),
                    fieldError.getField(), fieldError.getDefaultMessage());
        }
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<Violation> idNotFoundException(IdNotFoundException e) {
        log.error("Некорректный запрос: {} с id={} не найден", e.getItemType(), e.getId());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Violation("id", e.getMessage()));
    }
}
