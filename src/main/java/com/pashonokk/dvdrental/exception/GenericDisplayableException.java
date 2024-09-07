package com.pashonokk.dvdrental.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.Arrays;
import java.util.Objects;

@Getter
public class GenericDisplayableException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3486940341897263629L;
    protected final HttpStatus status;
    protected final String message;
    protected final String[] messageParams;

    public GenericDisplayableException(HttpStatus status, String message, String[] parameters, Exception e) {
        super(e);
        this.status = status;
        this.messageParams = getParams(parameters);
        this.message = message;
    }

    public GenericDisplayableException(HttpStatus status, String message, Throwable e) {
        super(e);
        this.status = status;
        this.messageParams = null;
        this.message = message;
    }

    public GenericDisplayableException(HttpStatus status, String message, String[] parameters) {
        this.status = status;
        this.messageParams = getParams(parameters);
        this.message = message;
    }

    public GenericDisplayableException(HttpStatus status, String message) {
        this.status = status;
        this.messageParams = null;
        this.message = message;
    }

    public GenericDisplayableException(HttpStatus status) {
        this.status = status;
        this.messageParams = null;
        this.message = null;
    }

    public GenericDisplayableException(HttpStatus status, Throwable cause) {
        super(cause);
        this.status = status;
        this.messageParams = null;
        this.message = null;
    }

    private static String[] getParams(String[] parameters) {
        final String[] params;
        if (Objects.nonNull(parameters)) {
            params = Arrays.copyOf(parameters,
                    parameters.length);
        } else {
            params = new String[]{};
        }
        return params;
    }

}
