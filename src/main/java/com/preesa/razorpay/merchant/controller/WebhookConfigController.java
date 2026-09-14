package com.preesa.razorpay.merchant.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.preesa.razorpay.merchant.dto.request.WebhookConfigRequest;
import com.preesa.razorpay.merchant.dto.response.WebhookConfigResponse;
import com.preesa.razorpay.merchant.security.MerchantContext;
import com.preesa.razorpay.merchant.service.WebhookConfigService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/merchants/webhooks")
public class WebhookConfigController {

    private final WebhookConfigService webhookConfigService;
    private final MerchantContext merchantContext;

    @PostMapping("/create")
    public ResponseEntity<WebhookConfigResponse> create(@Valid @RequestBody WebhookConfigRequest request) {
        return ResponseEntity.ok(webhookConfigService.create(merchantContext.getMerchantId(), request));
    }

    @GetMapping("/getAllWebhook")
    public ResponseEntity<List<WebhookConfigResponse>> getAllWebhook() {
        return ResponseEntity.ok(webhookConfigService.getAll(merchantContext.getMerchantId()));
    }

    @GetMapping("/{configId}")
    public ResponseEntity<WebhookConfigResponse> getById(@PathVariable UUID configId) {
        return ResponseEntity.ok(webhookConfigService.getById(merchantContext.getMerchantId(), configId));
    }

    @PutMapping("/update/{configId}")
    public ResponseEntity<WebhookConfigResponse> update(@PathVariable UUID configId,
            @RequestBody WebhookConfigRequest request) {
        return ResponseEntity.ok(webhookConfigService.update(merchantContext.getMerchantId(), configId, request));
    }

    @DeleteMapping("/delete/{configId}")
    public ResponseEntity<Void> delete(@PathVariable UUID configId) {
        webhookConfigService.delete(merchantContext.getMerchantId(), configId);
        return ResponseEntity.noContent().build();
    }


}
