package com.preesa.razorpay.merchant.cache;

import java.util.Optional;

public interface ApiKeyCache {

    public Optional<ApiKeyCacheEntry> get(String keyId);

    public void put(String keyId, ApiKeyCacheEntry entry);

    public void evict (String keyId);

}
