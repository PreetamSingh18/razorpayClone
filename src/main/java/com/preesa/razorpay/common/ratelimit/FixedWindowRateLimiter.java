package com.preesa.razorpay.common.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.rate-limit.method", havingValue = "fixed")
public class FixedWindowRateLimiter implements RateLimiter {

    private final StringRedisTemplate redisTemplate;

    @Override
    public RateLimitResult check(String key, int maxRequestsAllowed, long windowSeconds) {
       String redisKey= "ratelimit:fixed:"+key;

       Long count= redisTemplate.opsForValue().increment(redisKey);

       if(count == null){
           return RateLimitResult.allowed(maxRequestsAllowed);
       }
       if(count==1){
           redisTemplate.expire(redisKey, Duration.of(windowSeconds, ChronoUnit.SECONDS));
       }
       if(count>maxRequestsAllowed){
           Long ttl= redisTemplate.getExpire(redisKey);
           int retryAfterSecond = Math.toIntExact((ttl != null && ttl != 0) ? ttl : windowSeconds);
           return RateLimitResult.denied(retryAfterSecond);
       }
     return RateLimitResult.allowed((int) (maxRequestsAllowed-count));
    }
}
