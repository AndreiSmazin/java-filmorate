package ru.yandex.practicum.filmorate.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.valid.CorrectReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Film {
    private int id; // идентификатор

    @NotBlank
    private String name; // название

    @NotNull
    @Size(max = 200)
    private String description; // краткое описание

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    @CorrectReleaseDate
    private LocalDate releaseDate; // дата релиза

    @NotNull
    @Positive
    private int duration; // длительность фильма в минутах

    @NotNull
    private Mpa mpa;

    private List<Genre> genres;

    private int rate;
}
