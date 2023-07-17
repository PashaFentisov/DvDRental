package com.pashonokk.dvdrental.endpoint.dto;

import com.pashonokk.dvdrental.endpoint.BaseResponse;
import com.pashonokk.dvdrental.endpoint.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailureResponse extends BaseResponse {
    public FailureResponse(String message) {
        super(Status.FAIL, message);
    }

}