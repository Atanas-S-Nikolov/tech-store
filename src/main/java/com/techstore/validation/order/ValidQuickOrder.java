package com.techstore.validation.order;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = QuickOrderValidator.class)
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidQuickOrder {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
