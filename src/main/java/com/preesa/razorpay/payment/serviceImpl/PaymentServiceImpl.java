package com.preesa.razorpay.payment.serviceImpl;

import com.preesa.razorpay.common.enums.OrderStatus;
import com.preesa.razorpay.common.enums.PaymentStatus;
import com.preesa.razorpay.common.exceptions.BusinessRuleViolationException;
import com.preesa.razorpay.common.exceptions.ResourceNotFoundException;
import com.preesa.razorpay.payment.dto.request.PaymentInitRequest;
import com.preesa.razorpay.payment.dto.response.PaymentResponse;
import com.preesa.razorpay.payment.entity.OrderRecord;
import com.preesa.razorpay.payment.entity.Payment;
import com.preesa.razorpay.payment.gateway.PaymentGatewayRouter;
import com.preesa.razorpay.payment.gateway.dto.PaymentRequest;
import com.preesa.razorpay.payment.gateway.dto.PaymentResult;
import com.preesa.razorpay.payment.mapper.PaymentMapper;
import com.preesa.razorpay.payment.repository.OrderRepository;
import com.preesa.razorpay.payment.repository.PaymentRepository;
import com.preesa.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    private final PaymentGatewayRouter paymentGatewayRouter;

    private final PaymentMapper paymentMapper;

    /**
     * @param merchantId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {
        OrderRecord orderRecord = orderRepository.findByMerchantIdAndId(merchantId, request.orderId());

        if (orderRecord == null) {
            throw new ResourceNotFoundException("order", request.orderId());
        }

        if (orderRecord.getOrderStatus() != OrderStatus.CREATED && orderRecord.getOrderStatus() != OrderStatus.ATTEMPTED) {
            throw new BusinessRuleViolationException("ORDER_NOT_PAYABLE", "Order cannot be payable with status" + orderRecord.getOrderStatus());
        }
        orderRecord.setOrderStatus(OrderStatus.ATTEMPTED);
        orderRecord.setAttempts(orderRecord.getAttempts()+1);



        Payment payment= Payment.builder()
                .merchantId(merchantId)
                .order(orderRecord)
                .amount(orderRecord.getAmount())
                .status(PaymentStatus.CREATED)
                .method(request.paymentMethod())
                .methodDetails(request.methodDetails())
                .build();

         payment = paymentRepository.save(payment);

       PaymentResult result= paymentGatewayRouter.initiate(new PaymentRequest(payment.getId(),request.orderId(),merchantId,orderRecord.getAmount(),request.paymentMethod(),request.methodDetails()));

        switch (result) {
            case PaymentResult.Pending(String processorRef) -> payment.setProcessorReference(processorRef);
            case PaymentResult.Failure(String errorCode, String errorDescription) -> {
                payment.setErrorCode(errorCode);
                payment.setErrorDescription(errorDescription);
                payment.setStatus(PaymentStatus.FAILED);
            }
        }
        orderRepository.save(orderRecord);
        payment = paymentRepository.save(payment);

        return   paymentMapper.toResponse(payment);

    }
}
