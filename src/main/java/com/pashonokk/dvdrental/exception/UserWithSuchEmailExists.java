package com.pashonokk.dvdrental.exception;

public class UserWithSuchEmailExists extends RuntimeException {
    public UserWithSuchEmailExists(String message) {
        super(message);
    }
}
