package com.preesa.razorpay.merchant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.preesa.razorpay.merchant.dto.response.WebhookConfigResponse;
import com.preesa.razorpay.merchant.entity.MerchantWebhookConfig;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WebhookConfigMapper {

    @Mapping(target = "webhookSecret", source = "rawSecret")
    WebhookConfigResponse toResponse(MerchantWebhookConfig config, String rawSecret);
}