package com.servicedata.servicelogs.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameUniquenessValidator.class)
public @interface UniqueUsername {
    String message() default "{username.unique.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}