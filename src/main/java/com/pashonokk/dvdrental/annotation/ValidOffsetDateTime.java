package com.pashonokk.dvdrental.annotation;

import com.pashonokk.dvdrental.util.CustomOffsetDateTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomOffsetDateTimeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOffsetDateTime {
    String message() default "Invalid OffsetDateTime format, try using such format yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}