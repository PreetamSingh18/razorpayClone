package com.preesa.razorpay.vault.entity;

import com.preesa.razorpay.common.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "card_token")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardToken  extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    private UUID customerId;

    @Column(nullable = false, length = 50, unique = true)
    private String token;

    @ManyToOne( fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vault_card_id", nullable = false)
    private VaultCard vaultCard;

    private Instant revokedAt;

}
