package com.preesa.razorpay.payment.repository;

import com.preesa.razorpay.payment.entity.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {

    boolean existsByMerchantIdAndReceipt(UUID merchantId, String receipt);

    boolean existsByMerchantId(UUID merchantId);

    boolean existsByMerchantIdAndId(UUID merchantId, UUID orderId);

    OrderRecord findByMerchantIdAndId(UUID merchantId, UUID orderId);
}
