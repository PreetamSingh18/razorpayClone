package com.preesa.razorpay.payment.gateway;

import com.preesa.razorpay.common.enums.PaymentMethod;
import com.preesa.razorpay.payment.gateway.dto.PaymentRequest;
import com.preesa.razorpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

    Map<PaymentMethod, PaymentAdapter> paymentAdapterMap;

    public PaymentResult initiate(PaymentRequest request) {
        PaymentAdapter adapter = paymentAdapterMap.get(request.method());
        if (adapter == null) {
            throw new IllegalArgumentException("Adapter for " + request.method() + "  method doesn't found");
        }
       return adapter.initiate(request);
    }

}
