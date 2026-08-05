package com.preesa.razorpay.common.exceptions;

import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException{
    private final Integer retryAfterSeconds;
    private final Integer remainingRequests;

    public RateLimitException (String message, Integer retryAfterSeconds){
        super(message);
        this.remainingRequests=0;
        this.retryAfterSeconds=retryAfterSeconds;
    }
}
