package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.exception.TokenExpiredException;
import com.pashonokk.dvdrental.exception.UserNotFoundException;
import com.pashonokk.dvdrental.exception.UserWithSuchEmailExists;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralControllerAdvice {
    @ExceptionHandler(BigSizeException.class)
    public ResponseEntity<String> handleBigSizeExceptions(BigSizeException bigSizeException) {
        return ResponseEntity.badRequest().body(bigSizeException.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleBigSizeExceptions(UserNotFoundException userNotFoundException) {
        return ResponseEntity.badRequest().body(userNotFoundException.getMessage());
    }

    @ExceptionHandler(UserWithSuchEmailExists.class)
    public ResponseEntity<String> handleBigSizeExceptions(UserWithSuchEmailExists userWithSuchEmailExists) {
        return ResponseEntity.badRequest().body(userWithSuchEmailExists.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleBigSizeExceptions(TokenExpiredException tokenExpiredException) {
        return ResponseEntity.badRequest().body(tokenExpiredException.getMessage());
    }
}
