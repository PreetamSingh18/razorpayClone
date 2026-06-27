package com.preesa.razorpay.payment.dto.request;

import com.preesa.razorpay.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.support.ManagedProperties;

import java.util.Map;
import java.util.UUID;

public record PaymentInitRequest(

        @NotNull(message = "Order Id is required")
        UUID orderId,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        Map<String, Object>methodDetails

) {
}
