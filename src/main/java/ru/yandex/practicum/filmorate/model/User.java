package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.practicum.filmorate.valid.CorrectLogin;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    private final int id; // идентификатор

    @NotNull
    @Email
    private final String email; // адрес электронной почты

    @NotNull
    @CorrectLogin
    private final String login; // логин

    private final String name; // имя

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    @Past
    private final LocalDate birthday; // дата рождения
}
