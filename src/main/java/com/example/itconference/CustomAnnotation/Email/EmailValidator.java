package com.example.itconference.CustomAnnotation.Email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private final EmailValidator validator = new EmailValidator();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validator.isValid(value,context);
    }
}