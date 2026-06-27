package com.preesa.razorpay.payment.entity;


import com.preesa.razorpay.common.entity.BaseAuditEntity;
import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.common.enums.PaymentMethod;
import com.preesa.razorpay.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "payment",
        indexes = {
                @Index(name = "idx_merchant_payment_status" ,columnList = "merchant_id, status"),
                @Index(name = "idx_merchants_payment" ,columnList = "id, merchant_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment  extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id" , nullable = false)
    private OrderRecord order;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false,length = 50)
    private PaymentStatus status;

    @Embedded
    private Money amount;

    @Enumerated(value = EnumType.STRING)
    private PaymentMethod method;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "method_details", columnDefinition ="jsonb" )
    private Map<String,Object> methodDetails;

    @Column(nullable = false,length = 100)
    private String idempotencyKey;

    @Column(length = 100)
    private String bankReference; //UTR details

    @Column(length = 100)
    private String processorReference;

    @Column(length = 50)
    private String errorCode;

    @Column(length = 255)
    private String errorDescription;

    private Instant authorizedAt;
    private Instant capturedAt;
    private Instant failedAt;
    private Instant refundedAt;
    private Instant settledAt;

}
