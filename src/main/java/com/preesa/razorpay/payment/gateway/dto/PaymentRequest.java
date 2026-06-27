package com.preesa.razorpay.payment.gateway.dto;

import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.common.enums.PaymentMethod;
import com.preesa.razorpay.payment.entity.Payment;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(
        UUID paymentId,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentMethod method,
        Map<String, Object> methodDetails

) {
}
