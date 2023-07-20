package ru.yandex.practicum.filmorate.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Валидация логина пользователя. Логин не должен быть пустым или содержать пробелы.
 */
public class LoginValidator implements ConstraintValidator<CorrectLogin, String> {
    @Override
    public boolean isValid(String login, ConstraintValidatorContext context){
        if (login == null) {
            return true;
        }
        return (!login.isBlank()) && (!login.contains(" "));
    }
}
