package com.techstore.validation.favorites;

import com.techstore.model.dto.FavoritesDto;
import com.techstore.validation.product.ProductNameValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FavoritesValidator implements ConstraintValidator<ValidFavorites, FavoritesDto> {
    @Override
    public void initialize(ValidFavorites constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(FavoritesDto dto, ConstraintValidatorContext context) {
        ProductNameValidator productNameValidator = new ProductNameValidator();
        for (String name : dto.getProductNames()) {
            if (!productNameValidator.isValid(name, context)) {
                return false;
            }
        }
        return true;
    }
}
