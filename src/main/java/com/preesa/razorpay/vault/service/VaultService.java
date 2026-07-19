package com.preesa.razorpay.vault.service;

import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.preesa.razorpay.vault.dto.request.TokenizerRequest;
import com.preesa.razorpay.vault.dto.response.TokenizerResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

    TokenizerResponse tokenizer(TokenizerRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}
