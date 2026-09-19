package com.preesa.razorpay.operations.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor 
public class SettlementPaymentId {
    private UUID settlementId;
    private UUID paymentId;

}
