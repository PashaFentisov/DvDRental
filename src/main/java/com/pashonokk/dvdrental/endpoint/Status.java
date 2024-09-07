package com.pashonokk.dvdrental.endpoint;

import lombok.Getter;

@Getter
public enum Status {
    OK(200, "processed successfully"),
    CREATED(201, "request created"),
    ACCEPTED(202, "request accepted"),
    FAIL(500, "processing failed");

    private final Integer code;
    private final String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}