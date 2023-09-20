package ru.yandex.practicum.filmorate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@Builder
public class Friend {
    private int userId;
    private int friendId;
    @EqualsAndHashCode.Exclude
    private boolean isApproved;
}
