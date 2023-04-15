package com.techstore.validation.cart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.techstore.validation.builder.ConstraintViolationBuilder.buildConstraintViolation;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class CartKeyValidator implements ConstraintValidator<ValidCartKey, String> {
    @Override
    public void initialize(ValidCartKey constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String key, ConstraintValidatorContext context) {
        if (isBlank(key)) {
            buildConstraintViolation(context, "Cart key must not be blank");
            return false;
        }
        return true;
    }
}
