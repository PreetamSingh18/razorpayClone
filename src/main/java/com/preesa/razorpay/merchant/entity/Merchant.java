package com.preesa.razorpay.merchant.entity;


import ch.qos.logback.core.boolex.EvaluationException;
import com.preesa.razorpay.common.enums.BusinessType;
import com.preesa.razorpay.common.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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


    private String createdBy;
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;


}
