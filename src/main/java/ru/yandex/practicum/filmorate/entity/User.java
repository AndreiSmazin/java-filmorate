package ru.yandex.practicum.filmorate.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.valid.CorrectLogin;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class User {
    private int id; // идентификатор

    @NotNull
    @Email
    private String email; // адрес электронной почты

    @NotNull
    @CorrectLogin
    private String login; // логин

    private String name; // имя

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    @Past
    private LocalDate birthday; // дата рождения
}
