package com.preesa.razorpay.operations.entity;

import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.common.enums.SettlementStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "settlement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Embedded
    private Money amount;

    @Column(length = 100)
    private String bankReference; //UTR details

    @Enumerated(value = EnumType.STRING)
    private SettlementStatus status;

    private Instant createdAt;



}
