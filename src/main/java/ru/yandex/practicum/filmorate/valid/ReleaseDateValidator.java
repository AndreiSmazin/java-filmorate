package ru.yandex.practicum.filmorate.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * Валидация даты релиза фильма. Дата релиза не должна быть раньше 28 декабря 1895
 */
public class ReleaseDateValidator implements ConstraintValidator<CorrectReleaseDate, LocalDate> {
    private static final LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.parse("1895-12-28");

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext context) {
        if (releaseDate == null) {
            return true;
        }
        return !releaseDate.isBefore(FIRST_FILM_RELEASE_DATE);
    }
}
