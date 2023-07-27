package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class IdNotFoundException extends RuntimeException {
    private final long id;
    private final String itemType;

    public IdNotFoundException(Throwable cause, long id, String itemType) {
        super(cause);
        this.id = id;
        this.itemType = itemType;
    }

    public IdNotFoundException(String message, Throwable cause, long id, String itemType) {
        super(message, cause);
        this.id = id;
        this.itemType = itemType;
    }

    public IdNotFoundException(String message, long id, String itemType) {
        super(message);
        this.id = id;
        this.itemType = itemType;
    }
}
