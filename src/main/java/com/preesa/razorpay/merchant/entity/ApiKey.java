package com.preesa.razorpay.merchant.entity;

import ch.qos.logback.core.status.InfoStatus;
import com.preesa.razorpay.common.constants.RazorpayConstants;
import com.preesa.razorpay.common.entity.BaseAuditEntity;
import com.preesa.razorpay.common.enums.Environment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.resilience.annotation.EnableResilientMethods;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "api_key",
     indexes = {
        @Index(name = "idx_api_key_merchant_id" ,columnList = "merchant_id , enabled")
     })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKey extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(nullable = false, unique = true, length = 50)
    private String keyId;

    @Column(nullable = false, length = 200)
    private String keySecretHash;

    @Column(length = 200)
    private String previousKeySecretHash;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Environment environment;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled= true;

    private Instant lastUsedAt;
    private Instant rotatedAt;
    private Instant gracePeriodExpiresAt;

}
