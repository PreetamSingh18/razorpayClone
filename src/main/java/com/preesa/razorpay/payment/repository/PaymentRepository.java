package com.preesa.razorpay.payment.repository;

import com.preesa.razorpay.common.enums.PaymentStatus;
import com.preesa.razorpay.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findPaymentByOrderId(UUID orderId);

    Optional<Payment> findByIdAndMerchantId(UUID paymentId, UUID merchantId);

    List<Payment> findAllByStatusAndUpdatedAtBefore(PaymentStatus paymentStatus, Instant globalWindow);
}

