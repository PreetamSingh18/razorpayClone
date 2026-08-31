package com.preesa.razorpay.payment.repository;

import com.preesa.razorpay.payment.entity.OrderRecord;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {

    boolean existsByMerchantIdAndReceipt(UUID merchantId, String receipt);

    boolean existsByMerchantId(UUID merchantId);

    boolean existsByMerchantIdAndId(UUID merchantId, UUID orderId);

    OrderRecord findByMerchantIdAndId(UUID merchantId, UUID orderId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OrderRecord o where o.merchantId=:merchantId and o.id =:orderId")
    OrderRecord findByMerchantIdAndIdForUpdate(UUID merchantId, UUID orderId);
}

