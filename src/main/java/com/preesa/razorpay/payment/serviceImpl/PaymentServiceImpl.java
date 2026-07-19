package com.preesa.razorpay.payment.serviceImpl;

import com.preesa.razorpay.common.enums.OrderStatus;
import com.preesa.razorpay.common.enums.PaymentEvent;
import com.preesa.razorpay.common.enums.PaymentStatus;
import com.preesa.razorpay.common.exceptions.BusinessRuleViolationException;
import com.preesa.razorpay.common.exceptions.ResourceNotFoundException;
import com.preesa.razorpay.payment.dto.request.PaymentInitRequest;
import com.preesa.razorpay.payment.dto.response.PaymentResponse;
import com.preesa.razorpay.payment.entity.OrderRecord;
import com.preesa.razorpay.payment.entity.Payment;
import com.preesa.razorpay.payment.entity.PaymentTransitionLog;
import com.preesa.razorpay.payment.gateway.PaymentGatewayRouter;
import com.preesa.razorpay.payment.gateway.dto.PaymentRequest;
import com.preesa.razorpay.payment.gateway.dto.PaymentResult;
import com.preesa.razorpay.payment.mapper.PaymentMapper;
import com.preesa.razorpay.payment.processor.PaymentProcessorRouter;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.preesa.razorpay.payment.repository.OrderRepository;
import com.preesa.razorpay.payment.repository.PaymentRepository;
import com.preesa.razorpay.payment.service.PaymentService;
import com.preesa.razorpay.payment.statemachine.PaymentTransitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    private final PaymentGatewayRouter paymentGatewayRouter;

    private final PaymentMapper paymentMapper;

    private final PaymentProcessorRouter paymentProcessorRouter;

    private final PaymentTransitionService paymentTransitionService;

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

        paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_ATTEMPT);
       PaymentResult result= paymentGatewayRouter.initiate(new PaymentRequest(payment.getId(),request.orderId(),merchantId,orderRecord.getAmount(),request.paymentMethod(),request.methodDetails()));

        switch (result) {
            case PaymentResult.Pending(String processorRef) -> payment.setProcessorReference(processorRef);
            case PaymentResult.Failure(String errorCode, String errorDescription) -> {
                payment.setErrorCode(errorCode);
                payment.setErrorDescription(errorDescription);
              //  payment.setStatus(PaymentStatus.FAILED);
                paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
            }
            case PaymentResult.Success(String bankReference) -> {
                log.warn("Invalid State ");
                return null;
            }
        }
        orderRepository.save(orderRecord);
        payment = paymentRepository.save(payment);

        return   paymentMapper.toResponse(payment);

    }

    /**
     * @param merchantId
     * @param paymentId
     * @return
     */
    @Override
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {
        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId, merchantId).orElseThrow(() -> new ResourceNotFoundException("payment", paymentId));

        //payment.setStatus(PaymentStatus.CAPTURING);
        paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_REQUEST);


     //   PaymentResult result = paymentProcessorRouter.capture(payment.getMethod(), paymentId);
          PaymentResult result = paymentGatewayRouter.capture(payment.getMethod(), paymentId);

        if (result instanceof PaymentResult.Success success) {
           // payment.setStatus(PaymentStatus.CAPTURED);
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
            payment.setCapturedAt(Instant.now());
            log.info("Payment Captured successfully for payment Id {}", paymentId);
        } else if (result instanceof PaymentResult.Failure(String errorCode, String errorDescription)) {
            //payment.setStatus(PaymentStatus.AUTHORIZED);
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
            payment.setErrorCode(errorCode);
            payment.setErrorDescription(errorDescription);
            log.warn("Payment Capture failed for payment Id {}", paymentId);
        }

        payment = paymentRepository.save(payment);

        return paymentMapper.toResponse(payment);
    }

    /**
     * @param id
     * @param isApproved
     * @param bankRef
     * @param errorCode
     * @param errorDesc
     */
    @Override
    @Transactional
    public void resolveAuthorization(UUID id, boolean isApproved, String bankRef, String errorCode, String errorDesc) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Payment",id));

        if(payment.getStatus()!= PaymentStatus.AUTHORIZING){
            log.warn("Invalid payment status change attempt while resolveAuthorization");
            throw new BusinessRuleViolationException("INVALID_PAYMENT_STATUS","Invalid payment status change attempt while resolveAuthorization");
        }

        OrderRecord orderRecord = payment.getOrder();

        if(isApproved){
            paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_SUCCESS);
            payment.setBankReference(bankRef);
            payment.setAuthorizedAt(Instant.now());

            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_REQUEST);
            PaymentResult result = paymentGatewayRouter.capture(payment.getMethod(), id);

            if(result instanceof PaymentResult.Success){
                paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
                payment.setCapturedAt(Instant.now());
                orderRecord.setOrderStatus(OrderStatus.PAID);
            }
            else   if(result instanceof PaymentResult.Failure failure){
                paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorCode(failure.errorDescription());
            }

        }
        else{
            paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
            payment.setErrorCode(errorCode);
            payment.setErrorDescription(errorDesc);
        }

    paymentRepository.save(payment);
    orderRepository.save(orderRecord);

    //TODO: Kafka Inegration

    }
}
