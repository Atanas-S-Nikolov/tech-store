package com.techstore.validation.user;

import com.techstore.model.dto.UserDto;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.techstore.validation.builder.ConstraintViolationBuilder.buildConstraintViolation;
import static java.util.Objects.nonNull;

public class UserValidator implements ConstraintValidator<ValidUser, UserDto> {
    @Override
    public void initialize(ValidUser constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserDto user, ConstraintValidatorContext context) {
        RuleResult ruleResult = validatePasswords(user);

        if (!ruleResult.isValid()) {
            buildConstraintViolation(context, "Password is invalid");
            return false;
        }

        return true;
    }

    private RuleResult validatePasswords(UserDto user) {
        String password = user.getPassword();
        String newPassword = user.getNewPassword();
        PasswordValidator validator = new PasswordValidator(
                new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 2, false),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 2, false),
                new IllegalSequenceRule(EnglishSequenceData.USQwerty, 2, false),
                new WhitespaceRule()
        );
        RuleResult ruleResult = validator.validate(new PasswordData(password));
        if (nonNull(newPassword) && ruleResult.isValid()) {
            ruleResult = validator.validate(new PasswordData(newPassword));
        }
        return ruleResult;
    }
}
