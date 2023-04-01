package com.techstore.validation.user;

import com.techstore.model.enums.UserRole;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.techstore.validation.builder.ConstraintViolationBuilder.buildConstraintViolation;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {
    @Override
    public void initialize(ValidRole constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValidRole = false;
        List<String> roles = Arrays.stream(UserRole.values())
                .map(UserRole::getValue)
                .collect(Collectors.toList());
        for (String role : roles) {
            if (role.equals(value)) {
                isValidRole = true;
                break;
            }
        }
        if (!isValidRole) {
            String message = "User role must be one of the following values: " + String.join(", ", roles);
            buildConstraintViolation(context, message);
        }
        return isValidRole;
    }
}
