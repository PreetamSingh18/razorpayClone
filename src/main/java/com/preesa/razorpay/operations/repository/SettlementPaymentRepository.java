package com.preesa.razorpay.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.preesa.razorpay.operations.entity.SettlementPayment;
import com.preesa.razorpay.operations.entity.SettlementPaymentId;

public interface SettlementPaymentRepository extends JpaRepository<SettlementPayment, SettlementPaymentId> {
    
}
