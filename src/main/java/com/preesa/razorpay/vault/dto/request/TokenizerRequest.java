package com.preesa.razorpay.vault.dto.request;

import com.preesa.razorpay.vault.validation.ExpiryYearValid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.LuhnCheck;

import java.util.UUID;

public record TokenizerRequest(
        @NotBlank(message = "PAN is required")
        @LuhnCheck(message = "Invalid PAN ")
        @Pattern(regexp = "^[0-9]{13,19}$",message = "Incorrect length of PAN")
        String pan,


        @NotBlank(message = "CVV is required")
        @Pattern(regexp = "^[0-9]{3,5}$",message = "Incorrect length of CVV")
        String cvv,

        @NotBlank(message = "expiryMonth is required")
        @Pattern(regexp = "^[1-12]{1,2}$",message = "Incorrect length of expiryMonth it should be 1 to 12")
        String expiryMonth,

        @NotNull(message = "expiryYear is required")
        @ExpiryYearValid
        Integer  expiryYear,

        @NotNull(message = "customerId is required")
        UUID customerId,


       @Size(min = 3, message = "Atleast 3 character required")
        String cardHolderName

) {
}
