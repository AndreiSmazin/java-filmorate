package ru.yandex.practicum.filmorate.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация валидации даты релиза фильма
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateValidator.class)
@Documented
public @interface CorrectReleaseDate {
    String message() default "не должна быть раньше 28 декабря 1895";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
