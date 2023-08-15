package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class IdNotFoundException extends RuntimeException {
    private final String itemType;

    public IdNotFoundException(Throwable cause, String itemType) {
        super(cause);
        this.itemType = itemType;
    }

    public IdNotFoundException(String message, Throwable cause, String itemType) {
        super(message, cause);
        this.itemType = itemType;
    }

    public IdNotFoundException(String message, String itemType) {
        super(message);
        this.itemType = itemType;
    }
}
