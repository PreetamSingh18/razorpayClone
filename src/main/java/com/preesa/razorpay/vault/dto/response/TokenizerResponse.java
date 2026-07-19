package com.preesa.razorpay.vault.dto.response;

import com.preesa.razorpay.common.enums.CardBrand;

public record TokenizerResponse(
        String token,
        CardBrand brand,
        String lastFour,
        Integer expiryMonth,
        Integer expiryYear

) {
}
