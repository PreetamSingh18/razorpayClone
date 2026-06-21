package com.preesa.razorpay.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String errorCode,
        String errorMessage,
        Instant timestamp,
        List<fieldErrorResponse> fieldErrors
) {
    public record fieldErrorResponse (String field, String message){

    }

    public static  ErrorResponse of(String errorCode, String errorMessage){
       return new ErrorResponse(errorCode,errorMessage,Instant.now(),null);
    }
    public static  ErrorResponse of(String errorCode, String errorMessage,List<fieldErrorResponse>fieldErrors){
        return new ErrorResponse(errorCode,errorMessage,Instant.now(),fieldErrors);
    }
}
