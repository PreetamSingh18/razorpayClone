package com.preesa.razorpay.merchant.service;

import java.util.List;
import java.util.UUID;

import com.preesa.razorpay.merchant.dto.request.WebhookConfigRequest;
import com.preesa.razorpay.merchant.dto.response.WebhookConfigResponse;

public interface WebhookConfigService {
    WebhookConfigResponse create(UUID merchantId, WebhookConfigRequest request);

    List<WebhookConfigResponse> getAll (UUID merchantId);

    WebhookConfigResponse getById (UUID merchantId, UUID configId);

    WebhookConfigResponse update(UUID merchantId, UUID configId, WebhookConfigRequest request);

    void delete (UUID merchantId,UUID configId);
}
