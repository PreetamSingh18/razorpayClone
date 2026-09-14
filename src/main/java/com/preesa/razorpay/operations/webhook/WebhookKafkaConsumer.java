package com.preesa.razorpay.operations.webhook;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.preesa.razorpay.common.dto.WebhookTarget;
import com.preesa.razorpay.common.enums.WebhookEventStatus;
import com.preesa.razorpay.common.util.SignerUtil;
import com.preesa.razorpay.merchant.api.MerchantWebhookApi;
import com.preesa.razorpay.operations.entity.WebhookEvent;
import com.preesa.razorpay.operations.repository.DlqEventRepository;
import com.preesa.razorpay.operations.repository.WebhookEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
@Slf4j 
public class WebhookKafkaConsumer {

    private final MerchantWebhookApi merchantWebhookApi;
    private final JsonMapper jsonMapper;
    private final SignerUtil signerUtill;
    private final WebhookEventRepository webhookEventRepository;
    private final WebhookRetryQueue webhookRetryQueue;
    private final WebhookDqlRecorder recorder;

    @KafkaListener(topics = {
            "${app.kafka.topics.payments:payments.events}",
            "${app.kafka.topics.orders:orders.events}",
            "${app.kafka.topics.refunds:refunds.events}",
            "${app.kafka.topics.settlements:settlements.events}"
    })
    public void onWebhookEvent(ConsumerRecord<String, Map<String, Object>> record, Acknowledgment ack) {

        try{
        log.info("Consumer Record {}", record.toString());
        Map<String, Object> envelope = record.value();

        Map<String,Object> payload  = (Map<String, Object>) envelope.get("data");

        Object rawMerchantId = payload.get("merchantId");
        String eventType = (String) envelope.get("eventType");

        if(rawMerchantId ==  null){
             log.error("No Merchant found, skipping event :{}", eventType);
             ack.acknowledge();
             return;
        }

        UUID merchantId = UUID.fromString(rawMerchantId.toString()) ;

        List<WebhookTarget> activeConfigsForEvent = merchantWebhookApi.getActiveConfigsForEvent(merchantId, eventType);

        if(activeConfigsForEvent == null ){
            log.info("No Webhook target URL found for EventType {}", eventType);

            ack.acknowledge();
            return;
        }
         
        Map<String, Object> signatureData = Map.of("event", eventType, "payload", payload);
        String signatureJson = jsonMapper.writeValueAsString(signatureData);

        for(WebhookTarget target : activeConfigsForEvent){
            String signedSignature = signerUtill.sign(signatureJson,target.webhookSecret() );

            WebhookEvent event  = WebhookEvent.builder()
            .eventType(eventType)
            .payload(payload)
            .merchantId(merchantId)
            .signature(signedSignature)
            .nextRetryAt(Instant.now())
            .targetUrl(target.targetUrl())
            .status(WebhookEventStatus.PENDING)
            .build();

           event  = webhookEventRepository.save(event);

           webhookRetryQueue.enqueue(event.getId(), event.getNextRetryAt());
   
        }
        ack.acknowledge();
    }
    catch(DataAccessException | CannotCreateTransactionException e){
          log.error("Webhook consumer failed to process the record, offset {} due to ", record.offset() +" "+ e.getMessage());
    }
    catch(Exception logicExp){
           log.error("Webhook consumer failed to process the record, offset {} due to ", record.offset() +" "+ logicExp.getMessage());
           recorder.recordConsumerFailed(record,logicExp.getMessage());
           ack.acknowledge();
    }

    }

}
