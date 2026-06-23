package com.preesa.razorpay.payment.service;

import com.preesa.razorpay.payment.dto.request.CreateOrderRequest;
import com.preesa.razorpay.payment.dto.response.OrderResponse;
import com.preesa.razorpay.payment.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(UUID merchantId,CreateOrderRequest createOrderRequest);

    OrderResponse getOrderById(UUID orderId,UUID merchantId);

    String cancelOrderById(UUID orderId, UUID merchantId);

    List<PaymentResponse> getPaymentByOrderId(UUID orderId, UUID merchantId);
}
