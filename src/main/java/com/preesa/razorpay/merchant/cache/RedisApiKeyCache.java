package com.preesa.razorpay.merchant.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;


import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisApiKeyCache implements ApiKeyCache {

    private final StringRedisTemplate stringRedisTemplate;
    private final String API_PREFIX = "apiKey:";
    private final ObjectMapper objectMapper;
    private final Duration TTL = Duration.ofMinutes(5);



    @Override
    public Optional<ApiKeyCacheEntry> get(String keyId) {
        String key = API_PREFIX + keyId;
        try {

            String json = stringRedisTemplate.opsForValue().get(key);
            if (json == null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(json, ApiKeyCacheEntry.class));
        } catch (Exception e) {
            log.warn("Failed to fetch data from Redis for {}", key);
            return Optional.empty();
        }
    }

    @Override
    public void put(String keyId, ApiKeyCacheEntry entry) {
        String key = API_PREFIX + keyId;
        try {
            stringRedisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(entry), TTL);
        }
        catch (Exception e){
            log.warn("Failed to insert entry in Redis for {}", key);
        }
    }

    @Override
    public void evict(String keyId) {
        String key = API_PREFIX + keyId;
        try {
            stringRedisTemplate.delete(key);
        }
        catch (Exception e){
            log.warn("Failed to delete entry from Redis for {}", key);
        }
    }

}
