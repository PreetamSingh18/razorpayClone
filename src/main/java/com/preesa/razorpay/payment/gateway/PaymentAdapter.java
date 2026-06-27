package com.preesa.razorpay.payment.gateway;

import com.preesa.razorpay.payment.gateway.dto.PaymentRequest;
import com.preesa.razorpay.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest request);
}
