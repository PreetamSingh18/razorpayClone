package com.preesa.razorpay.merchant.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.preesa.razorpay.merchant.entity.MerchantWebhookConfig;

public interface WebhookConfigRepository extends JpaRepository<MerchantWebhookConfig,UUID> {
    
    List<MerchantWebhookConfig>findByMerchantId(UUID merchantId);

    Optional<MerchantWebhookConfig> findByIdAndMerchantId(UUID configId, UUID merchantId);

	List<MerchantWebhookConfig> findAllByMerchantIdAndEnabledTrue(UUID merchantId);
}
