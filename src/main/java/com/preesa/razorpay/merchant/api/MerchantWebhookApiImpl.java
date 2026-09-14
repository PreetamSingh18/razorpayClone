package com.preesa.razorpay.merchant.api;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.stereotype.Service;

import com.preesa.razorpay.common.dto.WebhookTarget;
import com.preesa.razorpay.merchant.entity.MerchantWebhookConfig;
import com.preesa.razorpay.merchant.repository.WebhookConfigRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class MerchantWebhookApiImpl implements MerchantWebhookApi {

    private final WebhookConfigRepository webhookConfigRepository; 
    private final BytesEncryptor bytesEncryptor;
    
    @Override
	public List<WebhookTarget> getActiveConfigsForEvent(UUID merchantId, String event) {

        return webhookConfigRepository.findAllByMerchantIdAndEnabledTrue(merchantId).stream()
                .filter(config -> config.isSubscribedTo(event))
                .map(config -> {

                    byte[] cipherBytes = Base64.getDecoder().decode(config.getWebhookSecret());
                    byte[] decryptedSecretBytes = bytesEncryptor.decrypt(cipherBytes);
                    return new WebhookTarget(config.getId(), config.getTargetUrl(),
                            new String(decryptedSecretBytes, StandardCharsets.UTF_8));
                })
                .toList();

    }

}
