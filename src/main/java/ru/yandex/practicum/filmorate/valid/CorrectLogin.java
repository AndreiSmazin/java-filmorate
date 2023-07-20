package ru.yandex.practicum.filmorate.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация валидации логина пользователя
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
@Documented
public @interface CorrectLogin {
    String message() default "не должен быть пустым или содержать пробелы";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
