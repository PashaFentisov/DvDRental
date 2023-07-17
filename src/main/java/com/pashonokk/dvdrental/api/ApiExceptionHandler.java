package com.pashonokk.dvdrental.api;

import com.pashonokk.dvdrental.exception.GenericDisplayableException;
import com.pashonokk.dvdrental.endpoint.ResponseProducer;
import com.pashonokk.dvdrental.endpoint.dto.FailureResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler implements ResponseProducer {
    @ExceptionHandler(GenericDisplayableException.class)
    protected ResponseEntity<Object> handleFinPlatException(@NotNull GenericDisplayableException e) {
        return fail(e, () -> new FailureResponse(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleFinPlatException(EntityNotFoundException e) {
        return fail(e, () -> new FailureResponse(e.getMessage()));
    }
}
