package com.pashonokk.dvdrental.api;

import com.pashonokk.dvdrental.endpoint.ResponseProducer;
import com.pashonokk.dvdrental.endpoint.dto.FailureResponse;
import com.pashonokk.dvdrental.endpoint.dto.ValidationFailedResponse;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.exception.GenericDisplayableException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler implements ResponseProducer {
    @ExceptionHandler(GenericDisplayableException.class)
    protected ResponseEntity<Object> handleFinPlatException(@NotNull GenericDisplayableException e) {
        return fail(e, () -> new FailureResponse(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(EntityValidationException.class)
    protected ResponseEntity<Object> handleEntityValidationException(@NotNull EntityValidationException e) {
        ArrayList<String> errorMessages = new ArrayList<>();
        e.getErrors().getFieldErrors()
                .forEach(error->errorMessages.add(error.getDefaultMessage()));
        return fail(e, () -> new ValidationFailedResponse(e.getMessage(), errorMessages));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        return fail(e, () -> new FailureResponse(e.getMessage()));
    }
}
