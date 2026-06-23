package com.preesa.razorpay.payment.serviceImpl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.preesa.razorpay.common.enums.OrderStatus;
import com.preesa.razorpay.common.exceptions.BusinessRuleViolationException;
import com.preesa.razorpay.common.exceptions.DuplicateResourceException;
import com.preesa.razorpay.common.exceptions.ResourceNotFoundException;
import com.preesa.razorpay.merchant.entity.Merchant;
import com.preesa.razorpay.merchant.repository.MerchantRepository;
import com.preesa.razorpay.payment.dto.request.CreateOrderRequest;
import com.preesa.razorpay.payment.dto.response.OrderResponse;
import com.preesa.razorpay.payment.dto.response.PaymentResponse;
import com.preesa.razorpay.payment.entity.OrderRecord;
import com.preesa.razorpay.payment.entity.Payment;
import com.preesa.razorpay.payment.mapper.OrderMapper;
import com.preesa.razorpay.payment.mapper.PaymentMapper;
import com.preesa.razorpay.payment.repository.OrderRepository;
import com.preesa.razorpay.payment.repository.PaymentRepository;
import com.preesa.razorpay.payment.service.OrderService;
import com.sun.jdi.connect.ListeningConnector;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;

import java.sql.ClientInfoStatus;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final MerchantRepository merchantRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    /**
     * @param merchantId
     * @return
     */
    @Override
    @Transactional
    public OrderResponse createOrder(UUID merchantId, CreateOrderRequest request) {
        Optional<Merchant> merchant= merchantRepository.findById(merchantId);
        if(merchant.isEmpty()){
            return null;
        }
        if(request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId,request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE", "Order already exists");
        }

        OrderRecord orderRecord = OrderRecord.builder()
                .merchantId(merchantId)
                .amount(request.amount())
                .receipt(request.receipt())
                .notes(request.notes())
                .createdBy(merchant.get().getName())
                .expireAt(request.expireAt()!=null ? request.expireAt(): Instant.now().plus(10, ChronoUnit.MINUTES))
                .build();

        orderRecord = orderRepository.save(orderRecord);
        return orderMapper.toResponse(orderRecord);
//        return new OrderResponse(orderRecord.getId(),merchantId,orderRecord.getAmount(),orderRecord.getReceipt(),orderRecord.getNotes(),
//                orderRecord.getOrderStatus(),orderRecord.getAttempts(),orderRecord.getExpireAt(),orderRecord.getCreatedAt(),null);


    }

    /**
     * @param orderId
     * @return
     */
    @Override
    public OrderResponse getOrderById(UUID orderId, UUID merchantId) {
       OrderRecord orderRecords= orderRepository.findByMerchantIdAndId(merchantId,orderId);
        if(orderRecords == null){
              throw new ResourceNotFoundException("ORDER",orderId);
        }
        return orderMapper.toResponse(orderRecords);
//        return new OrderResponse(orderRecords.getId(),merchantId,orderRecords.getAmount(),orderRecords.getReceipt(),orderRecords.getNotes()
//                ,orderRecords.getOrderStatus(),orderRecords.getAttempts(),orderRecords.getExpireAt(),orderRecords.getCreatedAt(),null);
    }

    /**
     * @param orderId
     * @param merchantId
     * @return
     */
    @Override
    public String cancelOrderById(UUID orderId, UUID merchantId) {
        OrderRecord orderRecords= orderRepository.findByMerchantIdAndId(merchantId,orderId);
        if(orderRecords == null){
            throw new ResourceNotFoundException("ORDER",orderId);
        }
        else if(orderRecords.getOrderStatus() == OrderStatus.CANCELLED || orderRecords.getOrderStatus() == OrderStatus.PAID){
            throw new BusinessRuleViolationException("ORDER_CANNOT_CANCE0L","Order cannot be cancelled with status "+orderRecords.getOrderStatus().name());
        }
        orderRecords.setOrderStatus(OrderStatus.CANCELLED);
        orderRecords.setUpdatedAt(Instant.now());
        orderRecords.setUpdatedBy(merchantId.toString());
        orderRepository.save(orderRecords);
        return "Order id : "+ orderId+" cancelled";
    }

    /**
     * @param orderId
     * @param merchantId
     * @return
     */
    @Override
    public List<PaymentResponse> getPaymentByOrderId(UUID orderId, UUID merchantId) {
        OrderRecord orderRecords= orderRepository.findByMerchantIdAndId(merchantId,orderId);
        List<Payment>paymentList = paymentRepository.findPaymentByOrderId(orderId);

        return paymentMapper.toResponseList(paymentList);
//        return paymentList.stream().map((payment)-> paymentMapper.toResponse(payment)).toList();
//        return new OrderResponse(orderRecords.getId(),merchantId,orderRecords.getAmount(),null,null
//                ,orderRecords.getOrderStatus(),orderRecords.getAttempts(),orderRecords.getExpireAt(),orderRecords.getCreatedAt(),paymentList);
    }
}
