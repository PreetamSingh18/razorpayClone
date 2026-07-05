package com.preesa.razorpay.payment.strategy;

import com.preesa.razorpay.payment.gateway.dto.PaymentResult;
import com.preesa.razorpay.payment.processor.PaymentProcessor;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;

import java.util.UUID;

public class CardPaymentProcessor implements PaymentProcessor {
    /**
     * @param request
     * @return
     */
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }

    /**
     * @param paymentId
     * @return
     */
    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("NET_BANKING_REF");
    }
}
