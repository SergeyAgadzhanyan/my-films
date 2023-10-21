package ru.yandex.practicum.filmorate.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class RealiseDateConstraintValidator implements ConstraintValidator<RealiseDate, LocalDate> {
    private static final LocalDate START_DATE = LocalDate.of(1895, 12, 27);

    @Override
    public void initialize(RealiseDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) return false;
        return !localDate.isBefore(START_DATE);
    }
}
