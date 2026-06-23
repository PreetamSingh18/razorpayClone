package com.preesa.razorpay.payment.dto.response;

import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.common.enums.PaymentMethod;
import com.preesa.razorpay.common.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.support.ManagedProperties;
import org.springframework.format.number.money.MonetaryAmountFormatter;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;


public record PaymentResponse(
        UUID id,
        UUID merchantId,
        UUID orderId,
        Money amount,
        PaymentStatus paymentStatus,
        PaymentMethod method,
        Map<String, Object> methodDetails,
        String bankReference,
        String errorCode,
        String errorDescription,
        Instant settledAt,
        Instant createdAt
) {
}
