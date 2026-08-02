package com.preesa.razorpay.merchant.serviceImpl;

import com.preesa.razorpay.common.exceptions.ResourceNotFoundException;
import com.preesa.razorpay.common.util.RandomizerUtil;
import com.preesa.razorpay.merchant.dto.request.ApiKeyCreateRequest;
import com.preesa.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.preesa.razorpay.merchant.dto.response.ApiKeyResponse;
import com.preesa.razorpay.merchant.entity.ApiKey;
import com.preesa.razorpay.merchant.entity.Merchant;
import com.preesa.razorpay.merchant.mapper.ApiKeyMapper;
import com.preesa.razorpay.merchant.repository.ApiKeyRepository;
import com.preesa.razorpay.merchant.repository.MerchantRepository;
import com.preesa.razorpay.merchant.service.ApiKeyService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.springframework.util.ObjectUtils.isCompatibleWithThrowsClause;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;

    private final ApiKeyRepository apiKeyRepository;

    private final ApiKeyMapper apiKeyMapper;

    private BCryptPasswordEncoder BCRYPT= new BCryptPasswordEncoder();

    @Override
    public ApiKeyCreateResponse create(UUID merchantId, ApiKeyCreateRequest apiKeyCreateRequest) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(()->new ResourceNotFoundException("merchant",merchantId)
        );

        String keyId= "rzp_"+ apiKeyCreateRequest.environment().name().toLowerCase()+"_"+ RandomizerUtil.randomBase64(24);
        String secretKey= RandomizerUtil.randomBase64(40);// TODO : encrypt

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(BCRYPT.encode(secretKey))
                .environment(apiKeyCreateRequest.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        //return new ApiKeyCreateResponse(apiKey.getId(),apiKey.getKeyId(),secretKey,apiKey.getEnvironment());
        return apiKeyMapper.toApiKeyCreateResponse(apiKey);
    }

    @Override
    public List<ApiKeyResponse> getListOfApiKey(UUID merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(()->new ResourceNotFoundException("merchant",merchantId)
                );

       List<ApiKey> apiKey= apiKeyRepository.findAllByMerchantId(merchantId);
//       return apiKey.stream().map(i->
//            new ApiKeyResponse(i.getId(),i.getKeyId(),i.getEnvironment(),i.getEnabled(),i.getLastUsedAt(),i.getCreatedAt())
//       ).toList();

        return apiKeyMapper.toApiKeyResponseList(apiKey);

    }

    @Override
    @Transactional
    public String revoke(UUID merchantId, String keyId) {
        String response;
        ApiKey apiKey = apiKeyRepository.findByKeyIdAndMerchantId(keyId, merchantId);
        if(isEmpty(apiKey)){
           throw new ResourceNotFoundException("apiKey",keyId);
        }
        else{
            apiKey.setEnabled(false);
            // Since we have added @Transactional , that will explicitly persist data in Entity and save at commit.
            // no need to add .save in this case
            //apiKeyRepository.save(apiKey);
            response="Access revoked for KeyId "+keyId;
        }

        return response;
    }

    @Override
    @Transactional
    public @Nullable ApiKeyCreateResponse rotateKey(UUID merchantId, String keyId) {
        ApiKey apiKey = apiKeyRepository.findByKeyIdAndMerchantId(keyId, merchantId);
        if(isEmpty(apiKey)){
            throw new ResourceNotFoundException("apiKey",keyId);
        }
        if(!apiKey.getEnabled()) throw new RuntimeException("Can't rotate a disabled key");
        String newRawSecretKey= RandomizerUtil.randomBase64(40);// TODO : encrypt
        apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
        apiKey.setKeySecretHash(BCRYPT.encode(newRawSecretKey));
        apiKey.setGracePeriodExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));
        apiKey.setUpdatedAt(Instant.now());
        apiKey.setUpdatedBy("SYSTEM");

//        return new ApiKeyCreateResponse(apiKey.getId(),apiKey.getKeyId(),newRawSecretKey,apiKey.getEnvironment());
        return apiKeyMapper.toApiKeyCreateResponse(apiKey);
    }
}
