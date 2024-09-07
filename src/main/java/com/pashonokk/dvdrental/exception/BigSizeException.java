package com.pashonokk.dvdrental.exception;

import org.springframework.http.HttpStatus;

public class BigSizeException extends GenericDisplayableException {
    public BigSizeException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
