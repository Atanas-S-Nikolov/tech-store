package com.techstore.validation.builder;

import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.nonNull;

public class ConstraintViolationBuilder {

    public static void buildConstraintViolation(ConstraintValidatorContext context, String errorMessage) {
        if (nonNull(context)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
        }
    }
}
