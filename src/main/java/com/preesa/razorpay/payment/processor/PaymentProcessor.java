package com.preesa.razorpay.payment.processor;

import com.preesa.razorpay.payment.gateway.dto.PaymentResult;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;

import java.util.UUID;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request) throws Exception;

    PaymentResult capture(UUID paymentId);
}
