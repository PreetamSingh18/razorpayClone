package com.preesa.razorpay.payment.gateway.adapter;

import com.preesa.razorpay.payment.gateway.PaymentAdapter;
import com.preesa.razorpay.payment.gateway.dto.PaymentRequest;
import com.preesa.razorpay.payment.gateway.dto.PaymentResult;
import com.preesa.razorpay.payment.processor.PaymentProcessorRouter;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.preesa.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {
    private final PaymentProcessorRouter paymentProcessorRouter;
    private final VaultService vaultService;

    /**
     * @param request
     * @return
     */
    @Override
    public PaymentResult initiate(PaymentRequest request) {
        try {
            log.info("Initiate Payment with CardPaymentAdapter with paymentId:{}", request.paymentId());

            String token = request.methodDetails().get("token").toString();
            PaymentProcessorResponse response = vaultService.charge(request.paymentId(), token, request.amount(), request.methodDetails());

            return switch (response) {
                case PaymentProcessorResponse.Pending pending ->
                        new PaymentResult.Pending(pending.processorReference());
                case PaymentProcessorResponse.Failure failure ->
                        new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
                case PaymentProcessorResponse.Success success ->
                        new PaymentResult.Success(success.transactionReference());
            };

        } catch (Exception e) {
            log.warn("Execption in Card payment adapter {}", e.getMessage());
            return new PaymentResult.Failure("CARD_PAYMENT_FAILURE", e.getMessage());
        }
    }

    /**
     * @param paymentId
     * @return
     */
    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("CARD_REF");
    }
}
