package com.techstore.validation.favorites;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = FavoritesValidator.class)
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFavorites {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
