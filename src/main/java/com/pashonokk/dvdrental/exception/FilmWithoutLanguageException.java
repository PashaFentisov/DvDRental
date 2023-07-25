package com.pashonokk.dvdrental.exception;

import org.springframework.http.HttpStatus;

public class FilmWithoutLanguageException extends GenericDisplayableException{
    public FilmWithoutLanguageException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
