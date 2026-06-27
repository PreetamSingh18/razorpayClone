package com.preesa.razorpay.payment.processor.dto;

import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.common.enums.PaymentMethod;

import javax.management.monitor.StringMonitor;
import java.util.Map;

public record PaymentProcessorRequest(
        PaymentMethod method,
        Map<String , Object> methodDetails,
        String pan,
        Money amount,
        String expiry
) {
}
