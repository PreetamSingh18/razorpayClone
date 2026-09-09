package com.preesa.razorpay.payment.outbox;

import com.preesa.razorpay.common.config.KafkaProperties;
import com.preesa.razorpay.common.enums.OutBoxStatus;
import com.preesa.razorpay.payment.entity.OutBoxEvent;
import com.preesa.razorpay.payment.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProperties kafkaProperties;
    private final OutboxResultHandler outboxResultHandler;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void poll() {

        List<OutBoxEvent> pendingEvents = outboxEventRepository.findByStatusOrderByCreatedAtAsc(OutBoxStatus.PENDING);

        for (OutBoxEvent event : pendingEvents) {
            try {
                String topic = kafkaProperties.topicFor(event.getAggregateType());
                String key = extractMerchantId(event.getPayload());

                Map<String, Object> envelope = Map.of(
                        "eventType", event.getEventType(),
                        "aggregateType", event.getAggregateType().name(),
                        "aggregateId", event.getAggregateId().toString(),
                        "date", event.getPayload()

                );

                String payloadJson = objectMapper.writeValueAsString(envelope);

                kafkaTemplate.send(topic, key, payloadJson).get(5, TimeUnit.SECONDS);
                outboxResultHandler.handleEventPublished(event);
            } catch (Exception e) {
                log.error("Outbox event failed , eventId: {}, attempts: {}", event.getId(),event.getAttempts());
                outboxResultHandler.handleEventFailed(event, e.getMessage());
            }

        }

    }


    private String extractMerchantId(Map<String, Object> payload) {
        Object value = payload.get("merchantId");
        return value != null ? value.toString() : "unknown";
    }
}
