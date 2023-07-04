package com.techstore.validation.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.techstore.validation.builder.ConstraintViolationBuilder.buildConstraintViolation;
import static java.util.Objects.isNull;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    private static final String BG_DIALING_CODE_PREFIX_REGEX = "^\\+359 ";
    private static final String DIALING_CODE_PREFIX_REGEX = "^\\+((\\d|-){1,5}) ";
    private static final String MOBILE_REGEX = DIALING_CODE_PREFIX_REGEX + "\\d{9}$";
    private static final String FIXED_LINE_REGEX = BG_DIALING_CODE_PREFIX_REGEX + "[1-9]\\d{8}$";
    private static final String FREEPHONE_REGEX = BG_DIALING_CODE_PREFIX_REGEX + "(800)\\d{5}$";
    private static final List<String> FORBIDDEN_NUMBERS = List.of("+359 888888888");

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (isNull(phone)) {
            buildConstraintViolation(context, "Phone number must not be blank");
            return false;
        }

        if (FORBIDDEN_NUMBERS.contains(phone)) {
            buildConstraintViolation(context, "This phone number is forbidden");
            return false;
        }

        Matcher fixedLinePattern = Pattern.compile(FIXED_LINE_REGEX).matcher(phone);
        Matcher mobilePattern = Pattern.compile(MOBILE_REGEX).matcher(phone);
        Matcher freePhoneMatcher = Pattern.compile(FREEPHONE_REGEX).matcher(phone);

        if (!(fixedLinePattern.matches() || mobilePattern.matches() || freePhoneMatcher.matches())) {
            buildConstraintViolation(context, "Invalid phone number");
            return false;
        }

        return true;
    }
}
