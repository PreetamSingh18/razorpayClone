package com.preesa.razorpay.vault.entity;

import com.preesa.razorpay.common.entity.BaseAuditEntity;
import com.preesa.razorpay.common.enums.CardBrand;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "vault_card")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VaultCard  extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 4)
    private String lastFour;

    @Column(nullable = false, length = 6)
    private String bin;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CardBrand brand;

    @Column(nullable = false)
    private byte[] encryptedPan;

    @Column(nullable = false)
    private byte[] encryptedDek;  // Secret key which used to encrypt pan

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false, length = 2)
    private String expiryMonth;

    @Column(nullable = false, length = 4)
    private String expiryYear;

    private Instant deletedAt;



}
