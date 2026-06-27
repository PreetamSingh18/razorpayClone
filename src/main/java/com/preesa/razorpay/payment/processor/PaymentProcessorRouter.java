package com.preesa.razorpay.payment.processor;

import com.preesa.razorpay.common.enums.PaymentMethod;
import com.preesa.razorpay.payment.gateway.PaymentAdapter;
import com.preesa.razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.preesa.razorpay.payment.gateway.adapter.NetBankingPaymentAdapter;
import com.preesa.razorpay.payment.gateway.adapter.UpiPaymentAdapter;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.preesa.razorpay.payment.strategy.CardPaymentProcessor;
import com.preesa.razorpay.payment.strategy.NetBankingPaymentProcessor;
import com.preesa.razorpay.payment.strategy.UpiPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Processor;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    private final Map<PaymentMethod, PaymentProcessor> paymentProcessorMap;

    PaymentProcessorResponse charge(PaymentProcessorRequest request){
        PaymentProcessor paymentProcessor= paymentProcessorMap.get(request.method());
        if (paymentProcessor == null) {
            throw new IllegalArgumentException("Processor for " + request.method() + "  method doesn't found");
        }
        return paymentProcessor.charge(request);
    }
}
