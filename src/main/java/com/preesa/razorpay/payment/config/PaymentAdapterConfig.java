package com.preesa.razorpay.payment.config;

import com.preesa.razorpay.common.enums.PaymentMethod;
import com.preesa.razorpay.payment.gateway.PaymentAdapter;
import com.preesa.razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.preesa.razorpay.payment.gateway.adapter.NetBankingPaymentAdapter;
import com.preesa.razorpay.payment.gateway.adapter.UpiPaymentAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentAdapterConfig {

    private final CardPaymentAdapter cardPaymentAdapter;
    private final NetBankingPaymentAdapter netBankingPaymentAdapter;
    private final UpiPaymentAdapter upiPaymentAdapter;

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap() {
        return Map.of(
                PaymentMethod.CARD, cardPaymentAdapter,
                PaymentMethod.NETBANKING, netBankingPaymentAdapter,
                PaymentMethod.UPI, upiPaymentAdapter
        );
    }

}
