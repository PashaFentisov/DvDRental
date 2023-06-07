package com.pashonokk.dvdrental.exception;

public class UserWithSuchEmailExists extends Exception {
    public UserWithSuchEmailExists(String message) {
        super(message);
    }
}
