package com.preesa.razorpay.payment.entity;

import com.preesa.razorpay.common.enums.PaymentEvent;
import com.preesa.razorpay.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_transition_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransitionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "from_status", nullable = false)
    private PaymentStatus fromStatus;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "to_status" , nullable = false)
    private PaymentStatus toStatus;

    @Column(length = 50, nullable = false)
    private String actor;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "event", nullable = false)
    private PaymentEvent event;

    private Instant createdAt;

}
