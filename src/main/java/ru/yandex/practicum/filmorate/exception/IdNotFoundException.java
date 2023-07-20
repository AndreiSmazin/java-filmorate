package ru.yandex.practicum.filmorate.exception;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(Throwable cause) {
        super(cause);
    }

    public IdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdNotFoundException(String message) {
        super(message);
    }
}
