package com.preesa.razorpay.common.ratelimit;

public record RateLimitResult(boolean isAllowed, int remainingRequests, int retryAfterSeconds) {
    public static RateLimitResult allowed(int remainingRequests) {
        return new RateLimitResult(true, remainingRequests, 0);
    }

    public static RateLimitResult denied(int retryAfterSeconds) {
        return new RateLimitResult(false, 0, retryAfterSeconds);
    }
}
