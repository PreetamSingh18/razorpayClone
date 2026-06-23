package com.preesa.razorpay.merchant.dto.response;

import ch.qos.logback.core.status.InfoStatus;
import com.preesa.razorpay.common.enums.Environment;

import java.time.Instant;
import java.util.UUID;


public record ApiKeyResponse(
        UUID id,
        String keyId,
        Environment environment,
        boolean enabled,
        Instant lastUsedAt,
        Instant createdAt

) {
}
