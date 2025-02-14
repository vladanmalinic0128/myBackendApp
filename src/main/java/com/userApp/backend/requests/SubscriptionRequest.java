package com.userApp.backend.requests;

public record SubscriptionRequest(
        Long id,
        boolean subscribed
) {
}
