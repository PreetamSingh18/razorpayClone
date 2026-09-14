package com.preesa.razorpay.merchant.dto.response;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record WebhookConfigResponse(
        UUID id,
        String targetUrl,
        String eventTypes,
        boolean enabled,
        String webhookSecret) {

}
