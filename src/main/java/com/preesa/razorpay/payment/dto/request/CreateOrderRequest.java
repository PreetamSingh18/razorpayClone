package com.preesa.razorpay.payment.dto.request;

import com.preesa.razorpay.common.entity.Money;

import java.time.Instant;
import java.util.Map;

public record CreateOrderRequest (
        Money amount,
        String receipt,
        Map<String,Object>notes,
        Instant expireAt
) {
}
