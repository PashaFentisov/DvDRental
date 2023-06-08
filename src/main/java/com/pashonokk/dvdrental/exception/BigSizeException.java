package com.pashonokk.dvdrental.exception;

public class BigSizeException extends RuntimeException {
    public BigSizeException(String message) {
        super(message);
    }
}
