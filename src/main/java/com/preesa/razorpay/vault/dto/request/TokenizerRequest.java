package com.preesa.razorpay.vault.dto.request;

import com.preesa.razorpay.vault.validation.ExpiryYearValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.LuhnCheck;

import java.util.UUID;

public record TokenizerRequest(
        @NotBlank(message = "PAN is required")
        @LuhnCheck(message = "Invalid PAN ")
        @Pattern(regexp = "^[0-9]{13,19}$",message = "Incorrect length of PAN")
        String pan,


        @NotNull(message = "CVV is required")
        @Pattern(regexp = "^[1-9]{3,5}$",message = "Incorrect length of CVV")
        Integer cvv,

        @NotNull(message = "expiryMonth is required")
        @Pattern(regexp = "^[1-12]{1,2}$",message = "Incorrect length of expiryMonth it should be 1 to 12")
        Integer expiryMonth,

        @NotNull(message = "expiryYear is required")
        @ExpiryYearValid
        Integer  expiryYear,

        @NotBlank(message = "customerId is required")
        UUID customerId,

        @Min(value = 3, message = "Atleast 3 character required")
        String cardHolderName

) {
}
