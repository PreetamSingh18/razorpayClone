package com.preesa.razorpay.operations.entity;

import com.preesa.razorpay.common.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table( name = "dle_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DlqEvent  extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @JdbcTypeCode(value = SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> payload;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "webhook_id")
    private WebhookEvent webhookEvent;

    private String finalError;

    private Instant movedAt;

    private Instant replayedAt;

}
