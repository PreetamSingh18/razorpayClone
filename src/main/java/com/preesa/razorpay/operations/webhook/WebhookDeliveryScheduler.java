package com.preesa.razorpay.operations.webhook;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.preesa.razorpay.common.enums.WebhookEventStatus;
import com.preesa.razorpay.operations.entity.WebhookEvent;
import com.preesa.razorpay.operations.repository.WebhookEventRepository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebhookDeliveryScheduler {

  @Value("${app.webhook.delivery.poll-batch-size:100}")
  private long batchSize;

  private final WebhookRetryQueue retryQueue;
  private final WebhookEventRepository webhookEventRepository;
  private final WebhookDeliverExecutor deliverExecutor;
  private ExecutorService virtualExecutorService;

  @PostConstruct
  void init() {
    virtualExecutorService = Executors.newVirtualThreadPerTaskExecutor();
  }

  @PreDestroy
  void destory() {
    virtualExecutorService.shutdown();
  }

  @Scheduled(fixedDelay = 1000)
  public void pollAndDeliver() {
    Set<UUID> due = retryQueue.pollDue(batchSize);

    if (due == null || due.isEmpty()) {
      log.info("No Due Events are present to deliver ");
      return;
    }

    for (UUID key : due) {
      virtualExecutorService.submit(() -> {
        deliverExecutor.deliver(key);
      });

    }

  }

  @Scheduled(fixedDelay = 10000)
  public void reconcileFromDatabase() {
    List<WebhookEvent> byStatusAndNextRetryBefore = webhookEventRepository
        .findByStatusAndNextRetryAtBefore(WebhookEventStatus.PENDING, Instant.now());

    for (WebhookEvent webhookEvent : byStatusAndNextRetryBefore) {
      retryQueue.enqueueIfAbsent(webhookEvent.getId(), webhookEvent.getNextRetryAt());
    }

  }

}
