package com.preesa.razorpay.payment.processor;

import com.preesa.razorpay.common.enums.PaymentMethod;
import com.preesa.razorpay.payment.gateway.dto.PaymentResult;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    private final Map<PaymentMethod, PaymentProcessor> paymentProcessorMap;

    public PaymentProcessorResponse charge(PaymentProcessorRequest request) throws Exception {
        PaymentProcessor paymentProcessor= paymentProcessorMap.get(request.method());
        if (paymentProcessor == null) {
            throw new IllegalArgumentException("Processor for " + request.method() + "  method doesn't found");
        }
        return paymentProcessor.charge(request);
    }

}
