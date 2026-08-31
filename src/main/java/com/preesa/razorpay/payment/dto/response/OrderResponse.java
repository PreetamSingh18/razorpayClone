package com.preesa.razorpay.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.common.enums.OrderStatus;
import com.preesa.razorpay.payment.entity.Payment;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderResponse(
        UUID orderId,
        UUID merchantId,
        UUID customerId,
        Money amount,
        String receipt,
        Map<String, Object> notes,
        OrderStatus orderStatus,
        Integer attempts,
        Instant expireAt,
        Instant createdAt

        ) {
}
