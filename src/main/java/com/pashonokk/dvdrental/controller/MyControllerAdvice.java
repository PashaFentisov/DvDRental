package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice {
    @ExceptionHandler(BigSizeException.class)
    public ResponseEntity<String> handleBigSizeExceptions(BigSizeException bigSizeException) {
        return ResponseEntity.badRequest().body(bigSizeException.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleBigSizeExceptions(UserNotFoundException userNotFoundException) {
        return ResponseEntity.badRequest().body(userNotFoundException.getMessage());
    }
}
