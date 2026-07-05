package com.preesa.razorpay.payment.service;

import com.preesa.razorpay.payment.dto.request.PaymentInitRequest;
import com.preesa.razorpay.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);

    PaymentResponse capture(UUID merchantId, UUID paymentId);
}
