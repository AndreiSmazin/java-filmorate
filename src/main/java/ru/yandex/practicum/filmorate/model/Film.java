package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.valid.CorrectReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private int id; // идентификатор

    @NotBlank
    private final String name; // название

    @NotNull
    @Size(max = 200)
    private final String description; // краткое описание

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    @CorrectReleaseDate
    private final LocalDate releaseDate; // дата релиза

    @NotNull
    @Positive
    private final int duration; // длительность фильма в минутах
}
