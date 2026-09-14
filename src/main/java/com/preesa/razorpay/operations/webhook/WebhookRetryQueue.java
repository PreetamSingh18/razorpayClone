package com.preesa.razorpay.operations.webhook;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component
@RequiredArgsConstructor 
public class WebhookRetryQueue {
    
    @Value("${app.webhook.delivery.redis-key:webhook-retry}")
   private  String key;

    private final StringRedisTemplate redis;

    public void enqueue(UUID webhookEventId, Instant retryAt){
        long time  = retryAt.toEpochMilli();

        redis.opsForZSet().add(key,webhookEventId.toString(),time);
        log.info("Enqueued a webhook event with id {}", webhookEventId.toString());
    }

    public Set<UUID> pollDue(long limit) {
        long now = Instant.now().toEpochMilli();
        // rangeByScoreWithScores fetch records on basis of there score(timestamp) along
        // with score.
        // where as rangeByScore only fetch on basis of index.
        Set<TypedTuple<String>> due = redis.opsForZSet().rangeByScoreWithScores(key, 0, now, 0, limit);

        if (due == null || due.isEmpty()) {
            return Set.of();
        }

        due.forEach(tuple -> redis.opsForZSet().remove(key, tuple.getValue()));
        return due.stream()
                .map(x -> UUID.fromString(x.getValue()))
                .collect(Collectors.toSet());
    }

     public void enqueueIfAbsent(UUID webhookEventId, Instant retryAt){
        long time  = retryAt.toEpochMilli();

        redis.opsForZSet().addIfAbsent(key,webhookEventId.toString(),time);
        log.info("Enqueued if absent a webhook event with id {}", webhookEventId.toString());
    }


}
