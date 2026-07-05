package com.preesa.razorpay.payment.controller;

import com.preesa.razorpay.payment.dto.request.PaymentInitRequest;
import com.preesa.razorpay.payment.dto.response.PaymentResponse;
import com.preesa.razorpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private PaymentService paymentService;

    UUID merchantId = UUID.fromString("b8667f5c-1b32-43c8-bb57-fec6bd350191");

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse>initiate(@Valid @RequestBody PaymentInitRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiate(merchantId,request));
    }
    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse>capture(UUID merchantId,UUID paymentId){
        return ResponseEntity.ok(paymentService.capture(merchantId,paymentId));
    }


}
