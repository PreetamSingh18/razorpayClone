package com.preesa.razorpay.merchant.serviceImpl;

import com.preesa.razorpay.common.exceptions.ResourceNotFoundException;
import com.preesa.razorpay.merchant.dto.request.ApiKeyCreateRequest;
import com.preesa.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.preesa.razorpay.merchant.entity.ApiKey;
import com.preesa.razorpay.merchant.entity.Merchant;
import com.preesa.razorpay.merchant.repository.ApiKeyRepository;
import com.preesa.razorpay.merchant.repository.MerchantRepository;
import com.preesa.razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;

    private final ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKeyCreateResponse create(UUID merchantId, ApiKeyCreateRequest apiKeyCreateRequest) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(()->new ResourceNotFoundException("merchant",merchantId)
        );

        String keyId= "rzp"+ apiKeyCreateRequest.environment().name().toLowerCase()+"big_random_key";
        String secretKey= "big_random_number"; // TODO : encrypt

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(secretKey)
                .environment(apiKeyCreateRequest.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(),apiKey.getKeyId(),secretKey,apiKey.getEnvironment());
    }
}
