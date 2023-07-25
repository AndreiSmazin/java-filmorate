package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class IdNotFoundException extends RuntimeException {
    private final int id;
    private final String itemType;

    public IdNotFoundException(Throwable cause, int id, String itemType) {
        super(cause);
        this.id = id;
        this.itemType = itemType;
    }

    public IdNotFoundException(String message, Throwable cause, int id, String itemType) {
        super(message, cause);
        this.id = id;
        this.itemType = itemType;
    }

    public IdNotFoundException(String message, int id, String itemType) {
        super(message);
        this.id = id;
        this.itemType = itemType;
    }
}
