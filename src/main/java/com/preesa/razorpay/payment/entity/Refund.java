package com.preesa.razorpay.payment.entity;


import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.common.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "refund")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @OneToOne
    private OrderRecord orderRecord;

    @Embedded
    private Money amount;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false,length = 50)
    private RefundStatus status = RefundStatus.PENDING;

    @Column(length = 100)
    private String bankReference; //UTR details

    @Column(length = 50)
    private String errorCode;

    @Column(length = 255)
    private String errorDescription;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition ="jsonb" )
    private Map<String,Object> notes;

    private Instant processedAt;


}
