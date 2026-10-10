package com.preesa.razorpay.payment.api;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import java.time.Instant;

import com.preesa.razorpay.common.enums.PaymentStatus;
import com.preesa.razorpay.payment.entity.Payment;
import com.preesa.razorpay.payment.repository.PaymentRepository;
import com.preesa.razorpay.common.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor
@Slf4j  
public class PaymentLookupServiceImpl implements  PaymentLookupService {

    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> findUnsettledCapturePayments(UUID merchantId) {

        List<Payment> unsettledPayments = paymentRepository.findByMerchantIdAndStatusForUpdate(merchantId,
                PaymentStatus.CAPTURED);

        log.info("Total unsettled payment of MerchantId :{} is count :{}", merchantId, unsettledPayments.size());

        return unsettledPayments;

    }
    
    @Override
    public void markPaymentAsSettled(UUID paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("PAYMENT", paymentId));

        payment.setStatus(PaymentStatus.SETTLED);
        payment.setSettledAt(Instant.now());

        paymentRepository.save(payment);
    }
    
}
