package com.preesa.razorpay.merchant.entity;

import com.preesa.razorpay.common.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customer",
        indexes = {
                @Index(name = "idx_customer_merchant_id" ,columnList = "merchant_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id", nullable=false)
    private Merchant merchant;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 50)
    private String email;

    @Column(length = 15)
    private String contactNumber;

    private String gstId;


}
