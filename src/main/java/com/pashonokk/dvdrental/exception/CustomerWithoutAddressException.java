package com.pashonokk.dvdrental.exception;

import org.springframework.http.HttpStatus;

public class CustomerWithoutAddressException extends GenericDisplayableException{
    public CustomerWithoutAddressException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
