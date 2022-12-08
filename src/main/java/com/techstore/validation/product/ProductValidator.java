package com.techstore.validation.product;

import com.techstore.model.dto.ProductDto;
import com.techstore.model.enums.ProductType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.techstore.model.enums.ProductCategory.AUDIO;
import static com.techstore.model.enums.ProductCategory.COMPUTERS_AND_LAPTOPS;
import static com.techstore.model.enums.ProductCategory.MICE_AND_KEYBOARDS;
import static com.techstore.model.enums.ProductCategory.TV_AND_MONITORS;
import static com.techstore.model.enums.ProductType.DESKTOP_PC;
import static com.techstore.model.enums.ProductType.EARPHONES;
import static com.techstore.model.enums.ProductType.HEADSET;
import static com.techstore.model.enums.ProductType.KEYBOARD;
import static com.techstore.model.enums.ProductType.LAPTOP;
import static com.techstore.model.enums.ProductType.MONITOR;
import static com.techstore.model.enums.ProductType.MOUSE;
import static com.techstore.model.enums.ProductType.TV;
import static com.techstore.model.enums.ProductType.TWS;
import static com.techstore.validation.builder.ConstraintViolationBuilder.buildConstraintViolation;
import static java.util.stream.Collectors.toList;

public class ProductValidator implements ConstraintValidator<ValidProduct, ProductDto> {
    private static final String DELIMITER = ", ";

    @Override
    public void initialize(ValidProduct constraintAnnotation) {
    }

    @Override
    public boolean isValid(ProductDto product, ConstraintValidatorContext context) {
        String category = product.getCategory();
        String type = product.getType();
        List<String> audioCategoryTypes = Arrays.asList(EARPHONES.getValue(), HEADSET.getValue(), TWS.getValue());
        List<String> computersAndLaptopsTypes = Arrays.asList(DESKTOP_PC.getValue(), LAPTOP.getValue());
        List<String> miceAndKeyboardsTypes = Arrays.asList(MOUSE.getValue(), KEYBOARD.getValue());
        List<String> tvAndMonitorsTypes = Arrays.asList(TV.getValue(), MONITOR.getValue());

        boolean isProductValid = false;
        if (category.equals(AUDIO.getValue())) {
            isProductValid = checkProductTypeForSpecificCategoryTypes(type, audioCategoryTypes, category, context);
        } else if (category.equals(COMPUTERS_AND_LAPTOPS.getValue())) {
            isProductValid = checkProductTypeForSpecificCategoryTypes(type, computersAndLaptopsTypes, category, context);
        } else if(category.equals(MICE_AND_KEYBOARDS.getValue())) {
            isProductValid = checkProductTypeForSpecificCategoryTypes(type, miceAndKeyboardsTypes, category, context);
        } else if (category.equals(TV_AND_MONITORS.getValue())) {
            isProductValid = checkProductTypeForSpecificCategoryTypes(type, tvAndMonitorsTypes, category, context);
        }
        return isProductValid;
    }

    private boolean checkProductTypeForSpecificCategoryTypes(
            String productType,
            Collection<String> types,
            String productCategory,
            ConstraintValidatorContext context
    ) {
        if (!types.contains(productType)) {
            String message = String.format("product.type '%s' for category '%s' must be one of the following values: %s",
                    productType, productCategory, String.join(DELIMITER, types));
            buildConstraintViolation(context, message);
            return false;
        }
        return true;
    }
}
