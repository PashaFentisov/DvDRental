package com.pashonokk.dvdrental.exception;

public class TokenExpiredException extends Exception{
    public TokenExpiredException(String message) {
        super(message);
    }
}
