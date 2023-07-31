package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.valid.CorrectReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private long id; // идентификатор

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

    private Set<Long> likes;
}
