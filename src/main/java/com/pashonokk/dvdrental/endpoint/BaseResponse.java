package com.pashonokk.dvdrental.endpoint;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseResponse {

    /**
     * response status
     */
    protected Status status;

    protected final String mdcId;

    /**
     * response message (can contain error message when it occurs)
     */
    protected String message;

    public BaseResponse(Status status, String message) {
        this.status = status;
        this.message = message;
        this.mdcId = MDCWrapper.getRequestId();
    }

    public BaseResponse() {
        this.mdcId = MDCWrapper.getRequestId();
    }
}