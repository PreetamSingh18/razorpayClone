package com.preesa.razorpay.payment.repository;

import com.preesa.razorpay.common.enums.PaymentStatus;
import com.preesa.razorpay.payment.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findPaymentByOrderId(UUID orderId);

    Optional<Payment> findByIdAndMerchantId(UUID paymentId, UUID merchantId);

    List<Payment> findAllByStatusAndUpdatedAtBefore(PaymentStatus paymentStatus, Instant globalWindow);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Payment p where p.id =:paymentId and p.merchantId =:merchantId")
    Optional<Payment> findByIdAndMerchantIdForUpdate(UUID paymentId, UUID merchantId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Payment p where p.id =:paymentId ")
    Optional<Payment> findByIdForUpdate(UUID id);
}

