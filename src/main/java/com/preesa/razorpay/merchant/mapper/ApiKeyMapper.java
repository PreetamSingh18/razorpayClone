package com.preesa.razorpay.merchant.mapper;

import com.preesa.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.preesa.razorpay.merchant.dto.response.ApiKeyResponse;
import com.preesa.razorpay.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    ApiKeyResponse toApiKeyResponse(ApiKey apiKey);

    List<ApiKeyResponse>toApiKeyResponseList(List<ApiKey> apiKey);

    @Mapping(target = "keySecret", source = "keySecretHash")
    ApiKeyCreateResponse toApiKeyCreateResponse(ApiKey apiKey);
}
