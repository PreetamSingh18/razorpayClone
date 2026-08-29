package com.preesa.razorpay.common.idempotency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class RedisIdempotencyStore implements IdempotencyStore {

    private final static String PREFIX = "idempotency:";
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * @param key
     * @param ttl
     * @return
     */
    @Override
    public boolean setIfAbsent(String key, Duration ttl) {
        try {
            boolean res = stringRedisTemplate.opsForValue().setIfAbsent(PREFIX + key, IN_PROGRESS, ttl);
            return res;
        } catch (DataAccessException e) {
            log.warn("Idempotency Store unavailable , failing open for key{}", key,e);
            return true;
        }
    }

    /**
     * @param key
     */
    @Override
    public void delete(String key) {
        try {
            stringRedisTemplate.delete(PREFIX + key);
        } catch (DataAccessException e) {
            log.warn("failed to clear idempotency key{}", key, e);
        }
    }

    /**
     * @param key
     * @param value
     * @param ttl
     */
    @Override
    public void store(String key, String value, Duration ttl) {
        try {
            stringRedisTemplate.opsForValue().set(PREFIX + key, value, ttl);
        } catch (DataAccessException e) {
            log.warn("failed to store key in idempotency Store ", e);
        }

    }

    /**
     * @param key
     * @return
     */
    @Override
    public Optional<String> get(String key) {
        try {

            return Optional.ofNullable(stringRedisTemplate.opsForValue().get(PREFIX + key));
        } catch (DataAccessException e) {
            log.warn("failed to fetch key from idempotency Store", e);
            return Optional.empty();
        }
    }
}
