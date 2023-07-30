package ru.yandex.practicum.filmorate.exception;

public class IncorrectFriendIdException extends RuntimeException {
    public IncorrectFriendIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFriendIdException(Throwable cause) {
        super(cause);
    }

    public IncorrectFriendIdException(String message) {
        super(message);
    }
}
