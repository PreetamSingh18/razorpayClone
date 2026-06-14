package com.preesa.razorpay.payment.entity;

import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.common.enums.OrderStatus;
import com.preesa.razorpay.merchant.entity.Customer;
import com.preesa.razorpay.merchant.entity.Merchant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;

import java.lang.classfile.constantpool.MemberRefEntry;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table (name = "order_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.CREATED;

    @Embedded
    private Money amount;

    @Column(nullable = false)
    private int attempts=0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> notes;

    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;


}
