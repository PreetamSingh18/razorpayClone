package com.preesa.razorpay.common.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidStateTransitionException extends RuntimeException {
    private final String status;
    private final String event;

    public InvalidStateTransitionException(String status, String event) {

        super("Invalid status transition from "+status+" via event "+event );
        this.status = status;
        this.event = event;
    }


}
