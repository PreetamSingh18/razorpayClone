package com.preesa.razorpay.merchant.service;

import com.preesa.razorpay.merchant.dto.request.ApiKeyCreateRequest;
import com.preesa.razorpay.merchant.dto.response.ApiKeyCreateResponse;

import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse create(UUID merchantId, ApiKeyCreateRequest apiKeyCreateRequest);
}
