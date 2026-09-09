package com.preesa.razorpay.payment.outbox;

import com.preesa.razorpay.common.enums.OutBoxStatus;
import com.preesa.razorpay.payment.entity.OutBoxEvent;
import com.preesa.razorpay.payment.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class OutboxResultHandler {

    private static final Integer MAX_ATTEMPTS = 3 ;
    private  final OutboxEventRepository outboxEventRepository;

    @Transactional
    public void handleEventPublished(OutBoxEvent event) {
        event.setStatus(OutBoxStatus.PUBLISHED);
        event.setPublishedAt(Instant.now());
    }

    @Transactional
    public void handleEventFailed(OutBoxEvent event, String errorMessage) {
      event.setAttempts(event.getAttempts()+1);
      event.setLastError(errorMessage.length()<=1000 ? errorMessage: errorMessage.substring(0,1000));
      if(event.getAttempts()>= MAX_ATTEMPTS){
         event.setStatus(OutBoxStatus.FAILED);
      }
      outboxEventRepository.save(event);
    }
}
