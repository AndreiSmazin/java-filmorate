package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class FriendNotFoundException extends RuntimeException {
    private final long userId;
    private final long friendId;

    public FriendNotFoundException(Throwable cause, long userId, long friendId) {
        super(cause);
        this.userId = userId;
        this.friendId = friendId;
    }

    public FriendNotFoundException(String message, long userId, long friendId) {
        super(message);
        this.userId = userId;
        this.friendId = friendId;
    }

    public FriendNotFoundException(String message, Throwable cause, long userId, long friendId) {
        super(message, cause);
        this.userId = userId;
        this.friendId = friendId;
    }
}
