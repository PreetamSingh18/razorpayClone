package com.preesa.razorpay.common.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.support.StaticApplicationContext;


@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{
     public final String resource;
     public final Object identifier;

     public ResourceNotFoundException(String resource, Object identifier){
         super(resource+" not found "+ identifier);

         this.resource=resource;
         this.identifier=identifier;
     }
}
