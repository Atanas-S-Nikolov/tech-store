package com.techstore.validation.order;

import com.techstore.model.dto.QuickOrderDto;
import com.techstore.validation.user.EmailValidator;
import com.techstore.validation.user.PhoneValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.techstore.validation.builder.ConstraintViolationBuilder.buildConstraintViolation;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class QuickOrderValidator implements ConstraintValidator<ValidQuickOrder, QuickOrderDto> {
    @Override
    public void initialize(ValidQuickOrder constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(QuickOrderDto quickOrderDto, ConstraintValidatorContext context) {
        String email = quickOrderDto.getEmail();
        String phone = quickOrderDto.getPhone();
        boolean isInvalidEmail = !new EmailValidator().isValid(email, context);
        boolean isInvalidPhone = !new PhoneValidator().isValid(phone, context);

        if (isBlank(email) && isBlank(phone)) {
            buildConstraintViolation(context, "Email or phone number must be provided");
            return false;
        } else if (isNotBlank(email) && isInvalidEmail) {
            buildConstraintViolation(context, "Invalid email");
            return false;
        } else if (isNotBlank(phone) && isInvalidPhone) {
            buildConstraintViolation(context, "Invalid phone number");
            return false;
        }

        return true;
    }
}
