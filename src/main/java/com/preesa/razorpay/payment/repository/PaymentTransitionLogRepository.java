package com.preesa.razorpay.payment.repository;

import com.preesa.razorpay.payment.entity.PaymentTransitionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentTransitionLogRepository extends JpaRepository<PaymentTransitionLog, UUID> {
}
