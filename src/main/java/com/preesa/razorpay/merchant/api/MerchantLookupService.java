package com.preesa.razorpay.merchant.api;

import java.util.List;
import java.util.UUID;

import com.preesa.razorpay.common.dto.SettlementBankDetails;
import com.preesa.razorpay.common.dto.WebhookTarget;
import com.preesa.razorpay.operations.entity.SettlementPayment;

public interface MerchantLookupService {
    
    List<WebhookTarget> getActiveConfigsForEvent(UUID merchantId,String event);

    List<UUID> getAllActiveMerchantIds();

    SettlementBankDetails getSettlementBankDetails(UUID merchantId);

}
