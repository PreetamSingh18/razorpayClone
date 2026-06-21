package com.preesa.razorpay.merchant.dto.response;

import com.preesa.razorpay.common.enums.BusinessType;
import com.preesa.razorpay.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
        UUID uuid,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus

) {
}
