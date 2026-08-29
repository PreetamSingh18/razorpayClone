package com.preesa.razorpay.common.ratelimit.ratelimiterImpl;

import com.preesa.razorpay.common.ratelimit.RateLimitResult;
import com.preesa.razorpay.common.ratelimit.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/*
1.Create a unique Redis key for the API key/client.
2.Increment the request count in Redis.
3.If Redis is unavailable, allow the request.
4.If this is the first request, set the key's TTL = window duration.
5.Check if the request count exceeds the allowed limit.
6.If exceeded → deny the request and return Retry-After based on remaining TTL.
7.Otherwise → allow the request and return the remaining request count.
8.Once TTL expires, Redis removes the key and a new window starts.
*/



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

