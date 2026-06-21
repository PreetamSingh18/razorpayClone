package com.preesa.razorpay.common.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateResourceException  extends RuntimeException{
  private final  String errorCode;

    public DuplicateResourceException(String errorCode,  String message){
        super(message);
        this.errorCode=errorCode;
    }
}
