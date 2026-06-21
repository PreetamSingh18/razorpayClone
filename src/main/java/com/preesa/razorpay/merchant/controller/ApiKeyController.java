package com.preesa.razorpay.merchant.controller;


import com.preesa.razorpay.merchant.dto.request.ApiKeyCreateRequest;
import com.preesa.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.preesa.razorpay.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
