package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class LikeNotFoundException extends RuntimeException {
    private final long filmId;
    private final long userID;

    public LikeNotFoundException(Throwable cause, long filmId, long userID) {
        super(cause);
        this.filmId = filmId;
        this.userID = userID;
    }

    public LikeNotFoundException(String message, long filmId, long userID) {
        super(message);
        this.filmId = filmId;
        this.userID = userID;
    }

    public LikeNotFoundException(String message, Throwable cause, long filmId, long userID) {
        super(message, cause);
        this.filmId = filmId;
        this.userID = userID;
    }
}
