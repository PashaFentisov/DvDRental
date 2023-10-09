package com.pashonokk.dvdrental.util;

import com.pashonokk.dvdrental.annotation.ValidOffsetDateTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.OffsetDateTime;

public class CustomOffsetDateTimeValidator implements ConstraintValidator<ValidOffsetDateTime, OffsetDateTime> {
    @Override
    public boolean isValid(OffsetDateTime value, ConstraintValidatorContext context) {
        try {
            if(value==null){
                throw new NullPointerException();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}