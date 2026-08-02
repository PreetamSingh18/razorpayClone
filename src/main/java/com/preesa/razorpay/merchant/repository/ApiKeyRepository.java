package com.preesa.razorpay.merchant.repository;

import com.preesa.razorpay.merchant.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {

    List<ApiKey> findAllByMerchantId(UUID merchantId);
    ApiKey findByKeyIdAndMerchantId(String keyId, UUID merchantId);

    Optional<ApiKey> findByKeyId(String keyId);
}
