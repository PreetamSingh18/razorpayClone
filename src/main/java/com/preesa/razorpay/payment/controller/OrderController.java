package com.preesa.razorpay.payment.controller;

import com.preesa.razorpay.merchant.security.MerchantContext;
import com.preesa.razorpay.payment.dto.request.CreateOrderRequest;
import com.preesa.razorpay.payment.dto.response.OrderResponse;
import com.preesa.razorpay.payment.dto.response.PaymentResponse;
import com.preesa.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MerchantContext merchantContext;


    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(merchantContext.getMerchantId(),createOrderRequest));
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId,merchantContext.getMerchantId()));
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrderById(@PathVariable UUID orderId){
        return ResponseEntity.ok(orderService.cancelOrderById(orderId,merchantContext.getMerchantId()));
    }

    @GetMapping("/getpayments/{orderId}")
    public ResponseEntity<List<PaymentResponse>>getPayments(@PathVariable UUID orderId){
        return ResponseEntity.ok(orderService.getPaymentByOrderId(orderId,merchantContext.getMerchantId()));
    }
}
