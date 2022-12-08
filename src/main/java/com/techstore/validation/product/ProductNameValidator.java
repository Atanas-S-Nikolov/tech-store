package com.techstore.validation.product;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.techstore.validation.builder.ConstraintViolationBuilder.buildConstraintViolation;
import static org.apache.commons.lang3.StringUtils.containsWhitespace;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ProductNameValidator implements ConstraintValidator<ValidProductName, String> {
    @Override
    public void initialize(ValidProductName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (isBlank(name)) {
            buildConstraintViolation(context, "Product name must not be blank");
            return false;
        }

        if (containsWhitespace(name)) {
            buildConstraintViolation(context, "Product name must not contains spaces");
            return false;
        }

        return true;
    }
}
