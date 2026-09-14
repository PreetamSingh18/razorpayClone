package com.preesa.razorpay.merchant.api;

import java.util.List;
import java.util.UUID;

import com.preesa.razorpay.common.dto.WebhookTarget;

public interface MerchantWebhookApi {
    
    List<WebhookTarget> getActiveConfigsForEvent(UUID merchantId,String event);

}
