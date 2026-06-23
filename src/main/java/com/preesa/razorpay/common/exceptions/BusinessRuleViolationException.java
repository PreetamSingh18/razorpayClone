package com.preesa.razorpay.common.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessRuleViolationException extends RuntimeException {
    private final  String errorCode;

    public BusinessRuleViolationException(String errorCode,  String message){
        super(message);
        this.errorCode=errorCode;
    }
}
