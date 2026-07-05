package com.preesa.razorpay.payment.gateway.adapter;

import com.preesa.razorpay.payment.gateway.PaymentAdapter;
import com.preesa.razorpay.payment.gateway.dto.PaymentRequest;
import com.preesa.razorpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {
    /**
     * @param request
     * @return
     */
    @Override
    public PaymentResult initiate(PaymentRequest request) {

        return null;
    }
}
