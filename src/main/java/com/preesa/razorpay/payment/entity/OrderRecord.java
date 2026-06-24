package com.preesa.razorpay.payment.entity;

import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.common.enums.OrderStatus;
import com.preesa.razorpay.merchant.entity.Customer;
import com.preesa.razorpay.merchant.entity.Merchant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;

import java.lang.classfile.constantpool.MemberRefEntry;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table (name = "order_record",
        indexes = {
                @Index(name = "idx_order_status" ,columnList = "merchant_id, order_status"),
                @Index(name = "idx_merchants_orders" ,columnList = "id, merchant_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.CREATED;

    @Embedded
    private Money amount;

    @Column(length = 200)
    private String receipt;

    @Column(nullable = false)
    private int attempts=0;

    @JdbcTypeCode(value= SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> notes;

    private Instant expireAt;

    private String createdBy;

//    @CreationTimestamp
    @Builder.Default
    private Instant createdAt = Instant.now();
    private String updatedBy;

    @CreationTimestamp
    private Instant updatedAt;


}
