package com.preesa.razorpay.merchant.entity;


import ch.qos.logback.core.boolex.EvaluationException;
import com.preesa.razorpay.common.constants.RazorpayConstants;
import com.preesa.razorpay.common.enums.BusinessType;
import com.preesa.razorpay.common.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "merchant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Merchant {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 15)
    private String contactNumber;

    @Column(length = 200)
    private String websiteUrl;

    @Column(length = 50)
    private String businessName;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 50)
    private BusinessType businessType;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 50, nullable = false)
    @Builder.Default
    private MerchantStatus status = MerchantStatus.PENDING_KYC;

    private String gstId;

    @Column(length = 20)
    private String panId;

    @Column(length = 200)
    private String settlementBankAccount;

    @Column(length = 20)
    private String settlementBankIFSC;

    @Column(length = 200)
    private String settlementBankAccountHolderName;

    @Builder.Default
    private String createdBy = RazorpayConstants.SYSTEM;
    @Builder.Default
    private String updatedBy = RazorpayConstants.SYSTEM;

    @CreationTimestamp
    private Instant createdAt;

    @CreationTimestamp
    private Instant updatedAt;


}
