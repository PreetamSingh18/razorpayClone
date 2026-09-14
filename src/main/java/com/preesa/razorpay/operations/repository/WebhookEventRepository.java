package com.preesa.razorpay.operations.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.preesa.razorpay.common.enums.WebhookEventStatus;
import com.preesa.razorpay.operations.entity.WebhookEvent;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent,UUID> {

   List<WebhookEvent> findByStatusAndNextRetryAtBefore(WebhookEventStatus pending, Instant now);
    
}
