package com.preesa.razorpay.payment.outbox;

import com.preesa.razorpay.common.enums.EventAggregateType;
import com.preesa.razorpay.payment.entity.OutBoxEvent;
import com.preesa.razorpay.payment.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutBoxEventPublisher {

    private final OutboxEventRepository outboxEventRepository;

    public void publish(EventAggregateType aggregateType, UUID aggregateId,
                        String eventType, Map<String,Object> payload){
        OutBoxEvent outBoxEvent = OutBoxEvent.builder()
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .eventType(eventType)
                .payload(payload)
                .build();

        outboxEventRepository.save(outBoxEvent);
    }
}
