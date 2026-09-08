package com.preesa.razorpay.payment.entity;

import com.preesa.razorpay.common.entity.BaseAuditEntity;
import com.preesa.razorpay.common.enums.EventAggregateType;
import com.preesa.razorpay.common.enums.OutBoxStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.EmbeddedTable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OutBoxEvent extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventAggregateType aggregateType;

    @Column(nullable = false)
    private UUID aggregateId;

    @Column(nullable = false,length = 50)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false,columnDefinition = "jsonb")
    private Map<String,Object> payload;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private OutBoxStatus status = OutBoxStatus.PENDING;

    @Builder.Default
    @Column(nullable = false)
    private Integer attempts = 0;

    @Column(length = 1000)
    private String lastError;

    private Instant publishedAt;
}
