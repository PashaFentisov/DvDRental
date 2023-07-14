package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DvDRentalControllerAdvice {
    @ExceptionHandler(BigSizeException.class)
    public ResponseEntity<String> handleBigSizeExceptions(BigSizeException bigSizeException) {
        return ResponseEntity.badRequest().body(bigSizeException.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundExceptions(UserNotFoundException userNotFoundException) {
        return ResponseEntity.badRequest().body(userNotFoundException.getMessage());
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<String> handleUserExistsExceptions(UserExistsException userExistsException) {
        return ResponseEntity.badRequest().body(userExistsException.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleTokenExpiredExceptions(TokenExpiredException tokenExpiredException) {
        return ResponseEntity.badRequest().body(tokenExpiredException.getMessage());
    }
    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<String> handleCountryNotFoundExceptions(CountryNotFoundException countryNotFoundException) {
        return ResponseEntity.badRequest().body(countryNotFoundException.getMessage());
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCountryNotFoundExceptions(CustomerNotFoundException customerNotFoundException) {
        return ResponseEntity.badRequest().body(customerNotFoundException.getMessage());
    }
}
