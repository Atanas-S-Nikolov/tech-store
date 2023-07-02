package com.techstore.validation.user;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.WhitespaceRule;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.techstore.validation.builder.ConstraintViolationBuilder.buildConstraintViolation;
import static java.util.Objects.isNull;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (!validatePassword(password) ) {
            buildConstraintViolation(context, "Too weak password");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        if (isNull(password)) return false;
        org.passay.PasswordValidator validator = new org.passay.PasswordValidator(
                new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 3, false),
                new IllegalSequenceRule(EnglishSequenceData.USQwerty, 3, false),
                new WhitespaceRule()
        );
        return validator.validate(new PasswordData(password)).isValid();
    }
}
