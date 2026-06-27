package com.preesa.razorpay.payment.strategy;

import com.preesa.razorpay.payment.processor.PaymentProcessor;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;

public class UpiPaymentProcessor implements PaymentProcessor {
    /**
     * @param request
     * @return
     */
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}
