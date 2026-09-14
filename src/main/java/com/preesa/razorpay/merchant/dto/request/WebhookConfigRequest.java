package com.preesa.razorpay.merchant.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record WebhookConfigRequest(

    @Pattern(regexp = "^(https?)://.*$", message = "targetUrl must be a valid URL starting with http or https")
    @NotBlank(message = "targetUrl cannot be blank")
    @Size(max = 500)
    String targetUrl,

    
// Comma-separated fine-grained event type names (e.g. "PAYMENT_STATUS_CHANGED,REFUND_CREATED").
// Null/blank/"ALL" subscribes to every event type.

    @Size(max = 1000)
    String eventTypes

) {
    
}
