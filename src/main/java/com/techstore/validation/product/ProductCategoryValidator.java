package com.techstore.validation.product;

import com.techstore.model.enums.ProductCategory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.techstore.validation.builder.ConstraintViolationBuilder.buildConstraintViolation;

public class ProductCategoryValidator implements ConstraintValidator<ProductCategoryConstraint, String> {
    @Override
    public void initialize(ProductCategoryConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String productCategory, ConstraintValidatorContext context) {
        boolean isValidCategory = false;
        List<String> categories = Arrays.stream(ProductCategory.values())
                .map(ProductCategory::getValue)
                .collect(Collectors.toList());
        for (String category : categories) {
            if (productCategory.equals(category)) {
                isValidCategory = true;
                break;
            }
        }
        if (!isValidCategory) {
            String message = "ProductCategory must be one of the following values: " + String.join(", ", categories);
            buildConstraintViolation(context, message);
        }
        return isValidCategory;
    }
}
