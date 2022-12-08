package com.techstore.validation.product;

import com.techstore.model.enums.ProductType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.techstore.validation.builder.ConstraintViolationBuilder.buildConstraintViolation;

public class ProductTypeValidator implements ConstraintValidator<ProductTypeConstraint, String> {
    @Override
    public void initialize(ProductTypeConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String productType, ConstraintValidatorContext context) {
        boolean isValidType = false;
        List<String> types = Arrays.stream(ProductType.values())
                .map(ProductType::getValue)
                .collect(Collectors.toList());
        for (String type : types) {
            if (productType.equals(type)) {
                isValidType = true;
                break;
            }
        }
        if (!isValidType) {
            String message = "ProductType must be one of the following values: " + String.join(", ", types);
            buildConstraintViolation(context, message);
        }
        return isValidType;
    }
}
