package com.pashonokk.dvdrental.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import java.io.Serial;
@Getter
public class EntityValidationException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -3486940341897263629L;
    private final HttpStatus status;
    private final String message;
    private final Errors errors;
    public EntityValidationException(String message, Errors errors) {
        status = HttpStatus.BAD_REQUEST;
        this.message = message;
        this.errors = errors;
    }
}
