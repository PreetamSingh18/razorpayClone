package com.preesa.razorpay.merchant.entity;

import com.preesa.razorpay.common.entity.BaseAuditEntity;
import com.preesa.razorpay.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.resilience.annotation.EnableResilientMethods;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name= "app_user",
        indexes = {
                @Index(name = "idx_app_user_merchant_id" ,columnList = "merchant_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;







}
