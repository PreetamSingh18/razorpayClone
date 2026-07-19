package com.preesa.razorpay.vault.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class ExpiryYearValidator implements ConstraintValidator<ExpiryYearValid,Integer>{


    /**
     * @param expiryYear
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Integer expiryYear, ConstraintValidatorContext constraintValidatorContext) {
       if(expiryYear == null){
           return false;

       }
       int currentYear = Year.now().getValue();
       if(expiryYear>=currentYear){
           return true;
       }
       return false;
    }

}
