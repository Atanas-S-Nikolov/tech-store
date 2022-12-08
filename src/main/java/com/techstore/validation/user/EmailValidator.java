package com.techstore.validation.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.techstore.validation.builder.ConstraintViolationBuilder.buildConstraintViolation;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if(!org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email)) {
            buildConstraintViolation(context, "Email is invalid");
            return false;
        }
        return true;
    }
}
