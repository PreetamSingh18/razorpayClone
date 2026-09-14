package com.preesa.razorpay.merchant.entity;

import com.preesa.razorpay.common.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "merchant_webhook_config",
        indexes = {
                @Index(name = "idx_merchant_webhook" ,columnList = "merchant_id, enabled")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder 
@ToString 
public class MerchantWebhookConfig extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id", nullable=false)
    private Merchant merchant;

    @Column(nullable = false, length = 500)
    private String targetUrl;

    @Column(nullable = false)
    private String webhookSecret;

    // for which all event, webhook will be sent
    @Column(nullable = false)
    private String eventTypes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled= true;


   public boolean isSubscribedTo(String eventType) {
   if (eventTypes == null || eventTypes.isBlank()) {
       return true;
   }
   for (String type : eventTypes.split(",")) {
       String trimmed = type.trim();
       if (trimmed.equalsIgnoreCase("ALL") || trimmed.equalsIgnoreCase(eventType)) {
           return true;
       }
   }
   return false;
}




}
