package com.techstore.validation.builder;

import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.nonNull;

public class ConstraintViolationBuilder {

    public static void buildConstraintViolation(ConstraintValidatorContext context, String errorMessage, String... fields) {
        if (nonNull(context)) {
            context.disableDefaultConstraintViolation();
            var builder = context.buildConstraintViolationWithTemplate(errorMessage);
            for (String field : fields) {
                builder.addPropertyNode(field);
            }
            builder.addConstraintViolation();
        }
    }
}
