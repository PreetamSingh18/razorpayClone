package com.preesa.razorpay.vault.controller;

import com.preesa.razorpay.merchant.security.MerchantContext;
import com.preesa.razorpay.vault.dto.request.TokenizerRequest;
import com.preesa.razorpay.vault.dto.response.TokenizerResponse;
import com.preesa.razorpay.vault.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/vault")
public class VaultController {

    private final VaultService vaultService;
    private final MerchantContext merchantContext;

    @PostMapping("/tokenizer")
    public ResponseEntity<TokenizerResponse> tokenizer(@Valid @RequestBody TokenizerRequest request){
        return  ResponseEntity.status(HttpStatus.CREATED).body(vaultService.tokenizer(request,merchantContext.getMerchantId()));
    }

}
