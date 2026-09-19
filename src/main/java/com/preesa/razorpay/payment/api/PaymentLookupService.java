package com.preesa.razorpay.payment.api;

import java.util.List;
import java.util.UUID;

import com.preesa.razorpay.payment.entity.Payment;

public interface PaymentLookupService {

    public List<Payment> findUnsettledCapturePayments(UUID merchantId);
    
}
