package com.preesa.razorpay.operations.webhook;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.preesa.razorpay.common.enums.WebhookEventStatus;
import com.preesa.razorpay.operations.entity.DlqEvent;
import com.preesa.razorpay.operations.entity.WebhookEvent;
import com.preesa.razorpay.operations.repository.DlqEventRepository;
import com.preesa.razorpay.operations.repository.WebhookEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@Slf4j 
@RequiredArgsConstructor 
public class WebhookDqlRecorder {
    
    final private WebhookEventRepository webhookEventRepository;
    final private DlqEventRepository dlqEventRepository;


    @Transactional 
    public void saveInDlqPostAttemptExausted(WebhookEvent event,String err){

        log.info("Start Saving Event in DLQ for event_id {}", event.getId());
       
        DlqEvent dlqEvent = DlqEvent.builder()
        .webhookEvent(event)
        .merchantId(event.getMerchantId())
        .payload(event.getPayload())
        .finalError(err)
       // .movedAt(Instant.now())
        .build();

        dlqEventRepository.save(dlqEvent);
    }


    public void recordConsumerFailed(ConsumerRecord<String, Map<String, Object>> record, String message) {
        UUID merchantId = null;
        Map<String, Object> envelope = record.value();

        try {

            Map<String, Object> payload = (Map<String, Object>) envelope.get("data");
            Object rawMerchantId = payload.isEmpty() ? null : payload.get("merchantId");

            if (rawMerchantId != null) {
                merchantId = UUID.fromString(rawMerchantId.toString());
            }
        } catch (Exception ignored) {

        }

        DlqEvent dlqEvent = DlqEvent.builder()
                .finalError(message)
                .merchantId(merchantId)
                .payload(envelope != null ? envelope : Map.of())
                .webhookEvent(null)
                .build();

        dlqEventRepository.save(dlqEvent);

    }
}
