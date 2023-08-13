package com.pashonokk.dvdrental.endpoint.dto;

import com.pashonokk.dvdrental.endpoint.BaseResponse;
import com.pashonokk.dvdrental.endpoint.Status;
import lombok.Getter;

import java.util.List;
@Getter
public class ValidationFailedResponse extends BaseResponse {
    private List<String> errorMessages;
    public ValidationFailedResponse(String message, List<String> errorMessages) {
        super(Status.FAIL, message);
        this.errorMessages = errorMessages;
    }
}
