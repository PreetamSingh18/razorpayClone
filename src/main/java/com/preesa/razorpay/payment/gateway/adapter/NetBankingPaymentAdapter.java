package com.preesa.razorpay.payment.gateway.adapter;

import com.preesa.razorpay.common.enums.PaymentMethod;
import com.preesa.razorpay.payment.gateway.PaymentAdapter;
import com.preesa.razorpay.payment.gateway.dto.PaymentRequest;
import com.preesa.razorpay.payment.gateway.dto.PaymentResult;
import com.preesa.razorpay.payment.processor.PaymentProcessorRouter;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NetBankingPaymentAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter paymentProcessorRouter;


    /**
     * @param request
     * @return
     */
    @Override
    public PaymentResult initiate(PaymentRequest request) {
        try{
        log.info("Initiate Payment with NetBankingPaymentAdapter with paymentId:{}", request.paymentId());

        PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.noncard(request.paymentId(), PaymentMethod.NETBANKING, request.amount(), request.methodDetails());

        PaymentProcessorResponse response = paymentProcessorRouter.charge(paymentProcessorRequest);

        return switch (response) {
            case PaymentProcessorResponse.Pending pending ->
                    new PaymentResult.Pending(pending.processorReference());
            case PaymentProcessorResponse.Failure failure ->
                    new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
            case PaymentProcessorResponse.Success success ->
                    new PaymentResult.Success(success.transactionReference());
        };
        } catch (Exception e) {
            log.warn("Execption in net banking payment adapter {}", e.getMessage());
            return new PaymentResult.Failure("NET_BANKING_FAILURE", e.getMessage());
        }
    }
}
