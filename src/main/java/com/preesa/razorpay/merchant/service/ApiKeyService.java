package com.preesa.razorpay.merchant.service;

import com.preesa.razorpay.merchant.dto.request.ApiKeyCreateRequest;
import com.preesa.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.preesa.razorpay.merchant.dto.response.ApiKeyResponse;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse create(UUID merchantId, ApiKeyCreateRequest apiKeyCreateRequest);

    List<ApiKeyResponse> getListOfApiKey(UUID merchantId);

    String revoke(UUID merchantId, String keyId);

    ApiKeyCreateResponse rotateKey(UUID merchantId, String keyId);
}
