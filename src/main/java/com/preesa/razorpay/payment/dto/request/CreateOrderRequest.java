package com.preesa.razorpay.payment.dto.request;

import com.preesa.razorpay.common.entity.Money;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Map;

public record CreateOrderRequest(
        @NotNull(message = "Amount is required")
        Money amount,
        @Size(max = 100)
        String receipt,
        Map<String, Object> notes,
        Instant expireAt,
        @Valid
        CustomerDetails customer
) {
    public record CustomerDetails(
            @Size(max = 200)
            String name,
            @Email
            @Size(max = 200)
            String email,

            @Size(max = 20)
            String phone
    ) {

    }
}
