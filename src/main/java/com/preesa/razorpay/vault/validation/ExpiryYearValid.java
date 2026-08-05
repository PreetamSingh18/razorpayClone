package com.preesa.razorpay.vault.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.List;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint( validatedBy = {ExpiryYearValidator.class})
public @interface ExpiryYearValid {
    String message() default "Expiry Year can not be in Past";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
