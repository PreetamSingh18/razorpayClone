package com.preesa.razorpay.operations.webhook;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.preesa.razorpay.common.enums.WebhookEventStatus;
import com.preesa.razorpay.operations.entity.WebhookEvent;
import com.preesa.razorpay.operations.repository.WebhookEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@Slf4j 
@RequiredArgsConstructor 
public class WebhookDeliverExecutor {
    @Value("${app.webhook.delivery.signature-header:X-Razorpay-Signature}")
    private String signatureHeader;

    private final Integer MAX_ATTEMPS = 7;

    private static final List<Duration> BACKOFF = List.of(
       Duration.ofMinutes(1), Duration.ofMinutes(5), Duration.ofMinutes(30),
       Duration.ofHours(2), Duration.ofHours(8), Duration.ofHours(24));

    private final WebhookEventRepository webhookEventRepository;
    private final RestClient restClient;
    private final WebhookRetryQueue retryQueue;
    private final WebhookDqlRecorder recorder;

    @Transactional 
    public void deliver(UUID webhookId){
        Optional<WebhookEvent> events  = webhookEventRepository.findById(webhookId);

        if(!events.isPresent()){
            log.info("No events found in webhook table to deliver , event id {}",webhookId);
            return;
        }

        WebhookEvent event = events.get();

        if(event.getStatus().equals(WebhookEventStatus.DELIVERED) || event.getStatus().equals(WebhookEventStatus.DEAD) ){
            log.info("Can not deliver the event{} of Status {} ",webhookId, event.getStatus().name());
            return;
        }

        event.setAttempts(event.getAttempts()+1);
        event.setLastAttemptAt(Instant.now());

        try{
            var response = restClient.post()
        .uri(event.getTargetUrl())
        .header(signatureHeader, event.getSignature())
        .body(Map.of("event", event.getEventType(),"payload", event.getPayload()))
        .contentType(MediaType.APPLICATION_JSON)
        .retrieve()
        .toBodilessEntity();

        event.setLastResponseCode(response.getStatusCode().value());

        if(response.getStatusCode().is2xxSuccessful()){
            event.setDeliveredAt(Instant.now());
            event.setStatus(WebhookEventStatus.DELIVERED);
            webhookEventRepository.save(event);
            log.info("Successfully called Webhook for Merchant{} :",event.getMerchantId().toString());
             return;
        }
            handleAttemptFailed(event, "HTTP"+response.getStatusCode());

        }
        catch(RestClientException e){
            event.setLastResponseBody(e.getMessage());
            handleAttemptFailed(event, e.getMessage());
            log.error("Exception while calling webhook for merchant{} for event{}", event.getMerchantId().toString(),event.getEventType());

        }
         
    }

    private void handleAttemptFailed( WebhookEvent event, String err){

          event.setLastResponseBody(err);

          if(event.getAttempts()> MAX_ATTEMPS){
            event.setStatus(WebhookEventStatus.DEAD);
           recorder.saveInDlqPostAttemptExausted(event, err);
            return;
          }

          Duration time = BACKOFF.get(event.getAttempts()-1);   
          Instant nextRetryAt = Instant.now().plus(time);

          event.setStatus(WebhookEventStatus.FAILED);
          event.setNextRetryAt(nextRetryAt);
          webhookEventRepository.save(event);

          retryQueue.enqueue(event.getId(), nextRetryAt);

          log.error("Handling attempt failed for  webhook event :{} with attempts: {}, next retry at {}", event.getId(), event.getAttempts(), event.getNextRetryAt());

    }


    
}
