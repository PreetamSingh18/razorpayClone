package com.preesa.razorpay.payment.repository;

import com.preesa.razorpay.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findPaymentByOrderId(UUID orderId);
}

