package com.preesa.razorpay.merchant.controller;


import com.preesa.razorpay.merchant.dto.request.ApiKeyCreateRequest;
import com.preesa.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.preesa.razorpay.merchant.dto.response.ApiKeyResponse;
import com.preesa.razorpay.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping("/create")
    public ResponseEntity<ApiKeyCreateResponse>create(@PathVariable UUID merchantId, @RequestBody @Valid ApiKeyCreateRequest apiKeyCreateRequest){
           return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.create(merchantId,apiKeyCreateRequest));
    }

    @GetMapping("/getAllApiKey")
    public ResponseEntity<List<ApiKeyResponse>>getListOfApiKey(@PathVariable UUID merchantId){
        return ResponseEntity.ok(apiKeyService.getListOfApiKey(merchantId));
    }

    @DeleteMapping("/revoke/{keyId}")
    public ResponseEntity<String>revoke(@PathVariable UUID merchantId, @PathVariable String keyId){
        return ResponseEntity.ok(apiKeyService.revoke(merchantId,keyId));
    }

    @PostMapping("/rotate/{keyId}")
    public ResponseEntity<ApiKeyCreateResponse>rotateKey(@PathVariable UUID merchantId, @PathVariable String keyId){
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.rotateKey(merchantId,keyId));
    }
}
